$(function () {
    /* actions au démarrage */
    createMenu();
    checkWarnings();
    /****** Sélectionne les infos à charger au démarrage ***/
    if ($('body>section').hasClass('home')) {
        attachApart('1');
    } else {
        if (getURLParameter('id') != null) {
            var id = getURLParameter('id');
            attachRoom(id);
            attachPlugs(id);
        } else {
            $('body > section').append('Veuillez sélectionner un appartement.')
        }
    }
    $('.warning input').on('click', function () {
        if ($(this).hasClass('active')) {
            $(this).removeClass('active');
            $('.infoWarn').hide();
        } else {
            $(this).addClass('active');
            $('.infoWarn').show();
        }
    })

    /************* fonctions générales *******************/
    /*****************************************************/
    // retrieves the json from the API
    //@param selector The jQuery element where the trigger is set
    function getApi(selector, thing, id) {
        $.get('src/proxy.php', {
            thing: thing,
            id: id
        }, function (json) {
            $(selector).trigger('getApi', [json])
        });
    }
    // retrieves the dom of the timegraph.html
    //@param selector The jQuery element where the trigger is set
    function getTimegraph(selector, json) {
        var url = "src/timegraph.html"
        $.get(url, function (html) {
            var dom = $(html);
            $(selector).trigger('getGraph', [dom, json])
        });



    }

    function getURLParameter(name) {
        return decodeURIComponent((new RegExp('[?|&]' + name + '=' + '([^&;]+?)(&|#|;|$)').exec(location.search) || [, ""])[1].replace(/\+/g, '%20')) || null;
    }
    /************** Fonctions de calcul ******************/
    /*****************************************************/

    //Peuple le menu de navigation
    function createMenu() {
        getApi('.menu', 'rooms', '');
        $('.menu').on('getApi', function (event, rooms) {
            var ul = $('<ul>')
            $.each(rooms, function (index, room) {
                var li = $('<li>')
                $('<h2>').append(
                    $('<a>').attr('href', 'room.php?id=' + room.roomId).text(room.name)
                ).appendTo(li)
                var ul2 = $('<ul>')
                ul2.attr('id', room.roomId)
                $.each(room.plugs, function (i, plug) {
                    ul2.append(
                        $('<li>').append(
                            $('<a>').attr('href', 'room.php?id=' + room.roomId + '#' + plug.plugId).text(plug.name)
                        )
                    )
                })
                ul2.appendTo(li)
                li.appendTo(ul)
            })
            $('.menu').append(ul)
            //Gestion display underMenu
            if (getURLParameter('id') != null) {
                showUnderMenus(id);
                var selectedUl = $('#' + id).get();
                setWhiteArrow(selectedUl);
            } else {
                hideUnderMenus();
            }
        })

    }

    //Attache le graph général de l'appartement
    function attachApart(id) {
        var selector = '.home .overall'
        getApi(selector, 'apartments', id);
        $(selector).on('getApi', function (event, json) {
            getTimegraph(selector, json)
        })
        $(selector).on('getGraph', function (event, dom, json) {
            dom.find('h2').text(json.name)
            dom.find('.onoff').show();
            dom.find('.alwayson').hide();
            dom.find('.graph').attr('id', json.apartmentId)
            //console.log(dom.find('.graph').get())
            $(selector).append(dom);
            drawChart(dom);
        })
    }

    //Attache le graph général de la room actuelle
    function attachRoom(id) {
        var selector = '.room .overall'
        getApi(selector, 'rooms', id);
        $(selector).on('getApi', function (event, json) {
            getTimegraph(selector, json)
        })
        $(selector).on('getGraph', function (event, dom, json) {
            dom.find('h2').text(json.name)
            dom.find('.onoff').show();
            dom.find('.alwayson').hide();
            $(selector).append(dom);
        })
    }
    //Attache le graph détaillé de la plug
    function attachPlugs(id) {
        var selector = '.room .plug';
        getApi(selector, 'rooms', id);
        $(selector).on('getApi', function (event, json) {
            getTimegraph(selector, json);
        });
        $(selector).on('getGraph', function (event, dom, json) {
            var domString = "";
            var html = dom;
            $.each(json.plugs, function (index, plug) {
                var tempDOM = html.clone();
                tempDOM.attr('id', plug.plugId);
                tempDOM.find('h2').text(plug.name);

                if (plug.alwaysOn) {
                    tempDOM.find('.onoff').hide();
                    tempDOM.find('.alwayson').show();
                } else {
                    tempDOM.find('.onoff').show();
                    tempDOM.find('.alwayson').hide();
                }
                $(selector).append(tempDOM);
            });
        });
    }

    function checkWarnings() {
        getApi('.warning', 'warnings', '');
        $('.warning').on('getApi', function (event, json) {
            if (json[0]) {
                var sec = $('<section>').addClass('infoWarn')
                $.each(json, function (index, warning) {
                    sec.append($('<p>').text(warning.message))
                })
                sec.appendTo($('.warning')).hide()
            } else {
                $('.warning').hide();
            }
        })
    }
    /*-------------------------------------*/
    /*Gestion de l'interaction Menu - Début*/
    /*-------------------------------------*/
    //Flèche sur sous menu
    function setWhiteArrow(ul) {
        var liPosition = $(ul).position();
        $("#whiteArrow").css("top", liPosition.top);
    }

    //Flèche sur home
    $('.menu').on('click', 'h1', function () {
        $("#whiteArrow").css("top", '95px');
    });

    /*-------------------------------------*/
    /*Gestion de l'interaction Menu - Fin*/
    /*-------------------------------------*/
    /*-------------------------------------*/
    /*Gestion du bouton On/Off - Début*/
    /*-------------------------------------*/

    $('.home, .room').on('click', '.onoff .button', function () {
        if ($(this).hasClass('on')) {
            $(this).removeClass('on').addClass('off').val('OFF');
        } else {
            $(this).removeClass('off').addClass('on').val('ON');
        }
    });
    /*-------------------------------------*/
    /*Gestion du bouton On/Off - Fin*/
    /*-------------------------------------*/

    /*-------------------------------------*/
    /*Gestion de l'interaction Menu - Début*/
    /*-------------------------------------*/
    function hideUnderMenus() {
        $('.menu>ul>li>ul').hide();
    };

    function showUnderMenus(id) {
        hideUnderMenus();
        $("#" + id).show();
    }

})