<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ page isELIgnored = "false" %>

<sec:authorize access="!isAuthenticated()">
  <c:redirect url = "/anastasia/login"/>
</sec:authorize>

<!DOCTYPE html>
<html lang="ru">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0"> <!--metatextblock--><title>Anastasia Trader</title>
    <meta property="og:url" content="http://localhost8081.tilda.ws/page49487957.html">
    <meta property="og:title" content="/main">
    <meta property="og:description" content="">
    <meta property="og:type" content="website">
    <meta property="og:image"
          content="https://static.tildacdn.info/tild3439-6330-4032-a666-343436366666/-/resize/504x/-22111.png">
    <link rel="canonical" href="http://localhost8081.tilda.ws/page49487957.html"><!--/metatextblock-->
    <meta name="format-detection" content="telephone=no">
    <meta http-equiv="x-dns-prefetch-control" content="on">
    <link rel="dns-prefetch" href="https://ws.tildacdn.com/">
    <link rel="dns-prefetch" href="https://static.tildacdn.info/">
    <meta name="robots" content="nofollow">
    <link rel="icon" href="/anastasia/style/favicon_file.webp" sizes="any" type="image/svg+xml">
    <script type="text/javascript" async="" id="tildastatscript" src="/anastasia/style/tilda-stat-1.0.min.js"></script>
    <script src="/anastasia/style/tilda-fallback-1.0.min.js" async="" charset="utf-8"></script>
    <link rel="stylesheet" href="/anastasia/style/tilda-grid-3.0.min.css" type="text/css" media="all"
          onerror="this.loaderr=&#39;y&#39;;">
    <link rel="stylesheet" href="/anastasia/style/tilda-blocks-page49487957.min.css" type="text/css" media="all"
          onerror="this.loaderr=&#39;y&#39;;">
    <link rel="stylesheet" href="/anastasia/style/tilda-cards-1.0.min.css" type="text/css" media="all"
          onerror="this.loaderr=&#39;y&#39;;">
    <link rel="stylesheet" href="/anastasia/style/tilda-cover-1.0.min.css" type="text/css" media="all"
          onerror="this.loaderr=&#39;y&#39;;">
    <link rel="stylesheet" href="/anastasia/style/fonts-tildasans.css" type="text/css" media="all"
          onerror="this.loaderr=&#39;y&#39;;">
    <script nomodule="" src="/anastasia/style/tilda-polyfill-1.0.min.js" charset="utf-8"></script>
    <script type="text/javascript">function t_onReady(func) {
        if (document.readyState != 'loading') {
            func();
        } else {
            document.addEventListener('DOMContentLoaded', func);
        }
    }
    function t_onFuncLoad(funcName, okFunc, time) {
        if (typeof window[funcName] === 'function') {
            okFunc();
        } else {
            setTimeout(function() {
                t_onFuncLoad(funcName, okFunc, time);
            },(time || 100));
        }
    }</script>
    <script src="/anastasia/style/tilda-scripts-3.0.min.js" charset="utf-8" defer=""
            onerror="this.loaderr=&#39;y&#39;;"></script>
    <script src="/anastasia/style/tilda-blocks-page49487957.min.js" charset="utf-8" async=""
            onerror="this.loaderr=&#39;y&#39;;"></script>
    <script src="/anastasia/style/tilda-lazyload-1.0.min.js" charset="utf-8" async=""
            onerror="this.loaderr=&#39;y&#39;;"></script>
    <script src="/anastasia/style/tilda-menu-1.0.min.js" charset="utf-8" async=""
            onerror="this.loaderr=&#39;y&#39;;"></script>
    <script src="/anastasia/style/tilda-cards-1.0.min.js" charset="utf-8" async=""
            onerror="this.loaderr=&#39;y&#39;;"></script>
    <script src="/anastasia/style/tilda-cover-1.0.min.js" charset="utf-8" async=""
            onerror="this.loaderr=&#39;y&#39;;"></script>
    <script src="/anastasia/style/tilda-skiplink-1.0.min.js" charset="utf-8" async=""
            onerror="this.loaderr=&#39;y&#39;;"></script>
    <script src="/anastasia/style/tilda-events-1.0.min.js" charset="utf-8" async=""
            onerror="this.loaderr=&#39;y&#39;;"></script>
    <script type="text/javascript">window.dataLayer = window.dataLayer || [];</script>
    <script type="text/javascript">(function () {
        if((/bot|google|yandex|baidu|bing|msn|duckduckbot|teoma|slurp|crawler|spider|robot|crawling|facebook/i.test(navigator.userAgent))===false && typeof(sessionStorage)!='undefined' && sessionStorage.getItem('visited')!=='y' && document.visibilityState){
            var style=document.createElement('style');
            style.type='text/css';
            style.innerHTML='@media screen and (min-width: 980px) {.t-records {opacity: 0;}.t-records_animated {-webkit-transition: opacity ease-in-out .2s;-moz-transition: opacity ease-in-out .2s;-o-transition: opacity ease-in-out .2s;transition: opacity ease-in-out .2s;}.t-records.t-records_visible {opacity: 1;}}';
            document.getElementsByTagName('head')[0].appendChild(style);
            function t_setvisRecs(){
                var alr=document.querySelectorAll('.t-records');
                Array.prototype.forEach.call(alr, function(el) {
                    el.classList.add("t-records_animated");
                });
                setTimeout(function () {
                    Array.prototype.forEach.call(alr, function(el) {
                        el.classList.add("t-records_visible");
                    });
                    sessionStorage.setItem("visited", "y");
                }, 400);
            }
            document.addEventListener('DOMContentLoaded', t_setvisRecs);
        }
    })();</script>
    <style type="text/css">@media screen and (min-width: 980px) {.t-records {opacity: 0;}.t-records_animated {-webkit-transition: opacity ease-in-out .2s;-moz-transition: opacity ease-in-out .2s;-o-transition: opacity ease-in-out .2s;transition: opacity ease-in-out .2s;}.t-records.t-records_visible {opacity: 1;}}</style>
</head>
<body class="t-body" style="margin:0;"><!--allrecords-->
<div id="allrecords" class="t-records t-records_animated t-records_visible" data-hook="blocks-collection-content-node"
     data-tilda-project-id="9588603" data-tilda-page-id="49487957"
     data-tilda-formskey="c760884931208f612fcce878e9588603" data-tilda-cookie="no" data-tilda-lazy="yes"
     data-tilda-root-zone="com">
    <div id="rec752807944" class="r t-rec" style=" " data-animationappear="off" data-record-type="456"><!-- T456 -->
        <div id="nav752807944marker"></div>
        <div class="tmenu-mobile tmenu-mobile_positionfixed">
            <div class="tmenu-mobile__container">
                <div class="tmenu-mobile__burgerlogo"><a href="https://t.me/anastasia_tradebot" target="_blank"
                                                         rel="noopener"><img src="/anastasia/style/bot.webp"
                                                                             class="tmenu-mobile__imglogo"
                                                                             imgfield="img"
                                                                             alt="Телеграм-бот"></a></div>
                <button type="button" class="t-menuburger t-menuburger_first " aria-label="Навигационное меню"
                        aria-expanded="false"><span style="background-color:#fff;"></span><span
                        style="background-color:#fff;"></span><span style="background-color:#fff;"></span><span
                        style="background-color:#fff;"></span></button>
                <script>function t_menuburger_init(recid) {
                    var rec = document.querySelector('#rec' + recid);
                    if (!rec) return;
                    var burger = rec.querySelector('.t-menuburger');
                    if (!burger) return;
                    var isSecondStyle = burger.classList.contains('t-menuburger_second');
                    if (isSecondStyle && !window.isMobile && !('ontouchend' in document)) {
                        burger.addEventListener('mouseenter', function() {
                            if (burger.classList.contains('t-menuburger-opened')) return;
                            burger.classList.remove('t-menuburger-unhovered');
                            burger.classList.add('t-menuburger-hovered');
                        });
                        burger.addEventListener('mouseleave', function() {
                            if (burger.classList.contains('t-menuburger-opened')) return;
                            burger.classList.remove('t-menuburger-hovered');
                            burger.classList.add('t-menuburger-unhovered');
                            setTimeout(function() {
                                burger.classList.remove('t-menuburger-unhovered');
                            }, 300);
                        });
                    }
                    burger.addEventListener('click', function() {
                        if (!burger.closest('.tmenu-mobile') &&
                        !burger.closest('.t450__burger_container') &&
                        !burger.closest('.t466__container') &&
                        !burger.closest('.t204__burger') &&
                        !burger.closest('.t199__js__menu-toggler')) {
                            burger.classList.toggle('t-menuburger-opened');
                            burger.classList.remove('t-menuburger-unhovered');
                        }
                    });
                    var menu = rec.querySelector('[data-menu="yes"]');
                    if (!menu) return;
                    var menuLinks = menu.querySelectorAll('.t-menu__link-item');
                    var submenuClassList = ['t978__menu-link_hook', 't978__tm-link', 't966__tm-link', 't794__tm-link', 't-menusub__target-link'];
                    Array.prototype.forEach.call(menuLinks, function (link) {
                        link.addEventListener('click', function () {
                            var isSubmenuHook = submenuClassList.some(function (submenuClass) {
                                return link.classList.contains(submenuClass);
                            });
                            if (isSubmenuHook) return;
                            burger.classList.remove('t-menuburger-opened');
                        });
                    });
                    menu.addEventListener('clickedAnchorInTooltipMenu', function () {
                        burger.classList.remove('t-menuburger-opened');
                    });
                }
                t_onReady(function() {
                    t_onFuncLoad('t_menuburger_init', function(){t_menuburger_init('752807944');});
                });</script>
                <style>.t-menuburger {
                    position: relative;
                    flex-shrink: 0;
                    width: 28px;
                    height: 20px;
                    padding: 0;
                    border: none;
                    background-color: transparent;
                    outline: none;
                    -webkit-transform: rotate(0deg);
                    transform: rotate(0deg);
                    transition: transform .5s ease-in-out;
                    cursor: pointer;
                    z-index: 999;
                }
                /*---menu burger lines---*/
                .t-menuburger span {
                    display: block;
                    position: absolute;
                    width: 100%;
                    opacity: 1;
                    left: 0;
                    -webkit-transform: rotate(0deg);
                    transform: rotate(0deg);
                    transition: .25s ease-in-out;
                    height: 3px;
                    background-color: #000;
                }
                .t-menuburger span:nth-child(1) {
                    top: 0px;
                }
                .t-menuburger span:nth-child(2),
                .t-menuburger span:nth-child(3) {
                    top: 8px;
                }
                .t-menuburger span:nth-child(4) {
                    top: 16px;
                }
                /*menu burger big*/
                .t-menuburger__big {
                    width: 42px;
                    height: 32px;
                }
                .t-menuburger__big span {
                    height: 5px;
                }
                .t-menuburger__big span:nth-child(2),
                .t-menuburger__big span:nth-child(3) {
                    top: 13px;
                }
                .t-menuburger__big span:nth-child(4) {
                    top: 26px;
                }
                /*menu burger small*/
                .t-menuburger__small {
                    width: 22px;
                    height: 14px;
                }
                .t-menuburger__small span {
                    height: 2px;
                }
                .t-menuburger__small span:nth-child(2),
                .t-menuburger__small span:nth-child(3) {
                    top: 6px;
                }
                .t-menuburger__small span:nth-child(4) {
                    top: 12px;
                }
                /*menu burger opened*/
                .t-menuburger-opened span:nth-child(1) {
                    top: 8px;
                    width: 0%;
                    left: 50%;
                }
                .t-menuburger-opened span:nth-child(2) {
                    -webkit-transform: rotate(45deg);
                    transform: rotate(45deg);
                }
                .t-menuburger-opened span:nth-child(3) {
                    -webkit-transform: rotate(-45deg);
                    transform: rotate(-45deg);
                }
                .t-menuburger-opened span:nth-child(4) {
                    top: 8px;
                    width: 0%;
                    left: 50%;
                }
                .t-menuburger-opened.t-menuburger__big span:nth-child(1) {
                    top: 6px;
                }
                .t-menuburger-opened.t-menuburger__big span:nth-child(4) {
                    top: 18px;
                }
                .t-menuburger-opened.t-menuburger__small span:nth-child(1),
                .t-menuburger-opened.t-menuburger__small span:nth-child(4) {
                    top: 6px;
                }
                /*---menu burger first style---*/
                @media (hover), (min-width:0\0) {
                    .t-menuburger_first:hover span:nth-child(1) {
                        transform: translateY(1px);
                    }
                    .t-menuburger_first:hover span:nth-child(4) {
                        transform: translateY(-1px);
                    }
                    .t-menuburger_first.t-menuburger__big:hover span:nth-child(1) {
                        transform: translateY(3px);
                    }
                    .t-menuburger_first.t-menuburger__big:hover span:nth-child(4) {
                        transform: translateY(-3px);
                    }
                }
                /*---menu burger second style---*/
                .t-menuburger_second span:nth-child(2),
                .t-menuburger_second span:nth-child(3) {
                    width: 80%;
                    left: 20%;
                    right: 0;
                }
                @media (hover), (min-width:0\0) {
                    .t-menuburger_second.t-menuburger-hovered span:nth-child(2),
                    .t-menuburger_second.t-menuburger-hovered span:nth-child(3) {
                        animation: t-menuburger-anim 0.3s ease-out normal forwards;
                    }
                    .t-menuburger_second.t-menuburger-unhovered span:nth-child(2),
                    .t-menuburger_second.t-menuburger-unhovered span:nth-child(3) {
                        animation: t-menuburger-anim2 0.3s ease-out normal forwards;
                    }
                }
                .t-menuburger_second.t-menuburger-opened span:nth-child(2),
                .t-menuburger_second.t-menuburger-opened span:nth-child(3){
                    left: 0;
                    right: 0;
                    width: 100%!important;
                }
                /*---menu burger third style---*/
                .t-menuburger_third span:nth-child(4) {
                    width: 70%;
                    left: unset;
                    right: 0;
                }
                @media (hover), (min-width:0\0) {
                    .t-menuburger_third:not(.t-menuburger-opened):hover span:nth-child(4) {
                        width: 100%;
                    }
                }
                .t-menuburger_third.t-menuburger-opened span:nth-child(4) {
                    width: 0!important;
                    right: 50%;
                }
                /*---menu burger fourth style---*/
                .t-menuburger_fourth {
                    height: 12px;
                }
                .t-menuburger_fourth.t-menuburger__small {
                    height: 8px;
                }
                .t-menuburger_fourth.t-menuburger__big {
                    height: 18px;
                }
                .t-menuburger_fourth span:nth-child(2),
                .t-menuburger_fourth span:nth-child(3) {
                    top: 4px;
                    opacity: 0;
                }
                .t-menuburger_fourth span:nth-child(4) {
                    top: 8px;
                }
                .t-menuburger_fourth.t-menuburger__small span:nth-child(2),
                .t-menuburger_fourth.t-menuburger__small span:nth-child(3) {
                    top: 3px;
                }
                .t-menuburger_fourth.t-menuburger__small span:nth-child(4) {
                    top: 6px;
                }
                .t-menuburger_fourth.t-menuburger__small span:nth-child(2),
                .t-menuburger_fourth.t-menuburger__small span:nth-child(3) {
                    top: 3px;
                }
                .t-menuburger_fourth.t-menuburger__small span:nth-child(4) {
                    top: 6px;
                }
                .t-menuburger_fourth.t-menuburger__big span:nth-child(2),
                .t-menuburger_fourth.t-menuburger__big span:nth-child(3) {
                    top: 6px;
                }
                .t-menuburger_fourth.t-menuburger__big span:nth-child(4) {
                    top: 12px;
                }
                @media (hover), (min-width:0\0) {
                    .t-menuburger_fourth:not(.t-menuburger-opened):hover span:nth-child(1) {
                        transform: translateY(1px);
                    }
                    .t-menuburger_fourth:not(.t-menuburger-opened):hover span:nth-child(4) {
                        transform: translateY(-1px);
                    }
                    .t-menuburger_fourth.t-menuburger__big:not(.t-menuburger-opened):hover span:nth-child(1) {
                        transform: translateY(3px);
                    }
                    .t-menuburger_fourth.t-menuburger__big:not(.t-menuburger-opened):hover span:nth-child(4) {
                        transform: translateY(-3px);
                    }
                }
                .t-menuburger_fourth.t-menuburger-opened span:nth-child(1),
                .t-menuburger_fourth.t-menuburger-opened span:nth-child(4) {
                    top: 4px;
                }
                .t-menuburger_fourth.t-menuburger-opened span:nth-child(2),
                .t-menuburger_fourth.t-menuburger-opened span:nth-child(3) {
                    opacity: 1;
                }
                /*---menu burger animations---*/
                @keyframes t-menuburger-anim {
                    0% {
                        width: 80%;
                        left: 20%;
                        right: 0;
                    }
                    50% {
                        width: 100%;
                        left: 0;
                        right: 0;
                    }
                    100% {
                        width: 80%;
                        left: 0;
                        right: 20%;
                    }
                }
                @keyframes t-menuburger-anim2 {
                    0% {
                        width: 80%;
                        left: 0;
                    }
                    50% {
                        width: 100%;
                        right: 0;
                        left: 0;
                    }
                    100% {
                        width: 80%;
                        left: 20%;
                        right: 0;
                    }
                }</style>
            </div>
        </div>
        <style>.tmenu-mobile {
            background-color: #111;
            display: none;
            width: 100%;
            top: 0;
            z-index: 990;
        }
        .tmenu-mobile_positionfixed {
            position: fixed;
        }
        .tmenu-mobile__text {
            color: #fff;
        }
        .tmenu-mobile__container {
            min-height: 64px;
            padding: 20px;
            position: relative;
            box-sizing: border-box;
            display: -webkit-flex;
            display: -ms-flexbox;
            display: flex;
            -webkit-align-items: center;
            -ms-flex-align: center;
            align-items: center;
            -webkit-justify-content: space-between;
            -ms-flex-pack: justify;
            justify-content: space-between;
        }
        .tmenu-mobile__list {
            display: block;
        }
        .tmenu-mobile__burgerlogo {
            display: inline-block;
            font-size: 24px;
            font-weight: 400;
            white-space: nowrap;
            vertical-align: middle;
        }
        .tmenu-mobile__imglogo {
            height: auto;
            display: block;
            max-width: 300px!important;
            box-sizing: border-box;
            padding: 0;
            margin: 0 auto;
        }
        @media screen and (max-width: 980px) {
            .tmenu-mobile__menucontent_hidden {
                display: none;
                height: 100%;
            }
            .tmenu-mobile {
                display: block;
            }
        }
        @media screen and (max-width: 980px) {
            #rec752807944 .tmenu-mobile {
                background-color: #8f6482;
            }
            #rec752807944 .tmenu-mobile__menucontent_fixed {
                position: fixed;
                top: 64px;
            }
            #rec752807944 .t-menuburger {
                -webkit-order: 1;
                -ms-flex-order: 1;
                order: 1;
            }
        }</style>
        <style> #rec752807944 .tmenu-mobile__burgerlogo a { color: #ffffff; }</style>
        <style> #rec752807944 .tmenu-mobile__burgerlogo__title { color: #ffffff; }</style>
        <div id="nav752807944" class="t456 tmenu-mobile__menucontent_hidden tmenu-mobile__menucontent_fixed"
             style="background-color: rgba(220,58,170,0.10); " data-bgcolor-hex="#dc3aaa"
             data-bgcolor-rgba="rgba(220,58,170,0.10)" data-navmarker="nav752807944marker" data-appearoffset=""
             data-bgopacity-two="30" data-menushadow="" data-menushadow-css="0px 2px 3px rgba(0,11,48,0.25)"
             data-bgopacity="0.10" data-bgcolor-rgba-afterscroll="rgba(220,58,170,0.30)" data-menu-items-align="right"
             data-menu="yes">
            <div class="t456__maincontainer " style="">
                <div class="t456__leftwrapper" style="min-width:80px;width:80px;">
                    <div class="t456__logowrapper" style="display: block;"><a href="https://t.me/anastasia_tradebot"
                                                                              target="_blank" rel="noopener"><img
                            class="t456__imglogo t456__imglogomobile" src="/anastasia/style/bot.webp" imgfield="img"
                            style="max-width: 80px; width: 80px;" alt="Телеграм-бот"></a></div>
                </div>
                <nav class="t456__rightwrapper t456__menualign_right" style="">
                    <ul role="list" class="t456__list t-menu__list">
                        <li class="t456__list_item" style="padding:0 15px 0 0;"><a class="t-menu__link-item"
                                                                                   href="/anastasia"
                                                                                   data-menu-submenu-hook=""
                                                                                   data-menu-item-number="1">Торговля</a>
                        </li>
                        <li class="t456__list_item" style="padding:0 15px;"><a class="t-menu__link-item"
                                                                               href="/anastasia"
                                                                               data-menu-submenu-hook=""
                                                                               data-menu-item-number="2">Информация</a>
                        </li>
                        <li class="t456__list_item" style="padding:0 15px;"><a class="t-menu__link-item"
                                                                               href="/anastasia"
                                                                               data-menu-submenu-hook=""
                                                                               data-menu-item-number="3">Личный
                            кабинет</a></li>
                        <li class="t456__list_item" style="padding:0 0 0 15px;"><a class="t-menu__link-item"
                                                                                   href="/anastasia/logout"
                                                                                   data-menu-submenu-hook=""
                                                                                   data-menu-item-number="4">Выход</a>
                        </li>
                    </ul>
                </nav>
            </div>
        </div>
        <style>@media screen and (max-width: 980px) {
            #rec752807944 .t456__logowrapper{
                display: none!important;
            }
            #rec752807944 .t456__mobile-text{
                display: none;
            }
            #rec752807944 .t456__imglogo{
                padding: 0;
            }
        }</style>
        <style>@media screen and (max-width: 980px) {
            #rec752807944 .t456__leftcontainer{
                padding: 20px;
            }
        }
        @media screen and (max-width: 980px) {
            #rec752807944 .t456__imglogo{
                padding: 20px 0;
            }
        }</style>
        <script type="text/javascript">t_onReady(function() {
            t_onFuncLoad('t_menu__highlightActiveLinks', function () {
                t_menu__highlightActiveLinks('.t456__list_item a');
            });
            t_onFuncLoad('t_menu__findAnchorLinks', function () {
                t_menu__findAnchorLinks('752807944', '.t456__list_item a');
            });
        });
        t_onReady(function () {
            t_onFuncLoad('t_menu__setBGcolor', function () {
                t_menu__setBGcolor('752807944', '.t456');
                window.addEventListener('resize', function () {
                    t_menu__setBGcolor('752807944', '.t456');
                });
            });
            t_onFuncLoad('t_menu__interactFromKeyboard', function () {
                t_menu__interactFromKeyboard('752807944');
            });
        });</script>
        <script type="text/javascript">t_onReady(function () {
            t_onFuncLoad('t_menu__createMobileMenu', function () {
                t_menu__createMobileMenu('752807944', '.t456');
            });
        });</script>
        <style>#rec752807944 .t-menu__link-item{
            -webkit-transition: color 0.3s ease-in-out, opacity 0.3s ease-in-out;
            transition: color 0.3s ease-in-out, opacity 0.3s ease-in-out;
        }
        #rec752807944 .t-menu__link-item.t-active:not(.t978__menu-link){
            color:#7e058c !important; }
        #rec752807944 .t-menu__link-item:not(.t-active):not(.tooltipstered):hover {
            color: #6135d2 !important; }
        #rec752807944 .t-menu__link-item:not(.t-active):not(.tooltipstered):focus-visible{
            color: #6135d2 !important; }
        @supports (overflow:-webkit-marquee) and (justify-content:inherit)
        {
            #rec752807944 .t-menu__link-item,
            #rec752807944 .t-menu__link-item.t-active {
                opacity: 1 !important;
            }
        }</style>
        <style> #rec752807944 .t456__logowrapper a { color: #ffffff; } #rec752807944 a.t-menu__link-item { font-size: 28px; color: #ffffff; font-weight: 600; }</style>
        <style> #rec752807944 .t456__logo { color: #ffffff; }</style>
        <!--[if IE 8]>
        <style>#rec752807944 .t456 {
filter: progid:DXImageTransform.Microsoft.gradient(startColorStr='#D9dc3aaa', endColorstr='#D9dc3aaa');
}
        </style><![endif]-->
        <style>#rec752807944 .t456 {box-shadow: 0px 2px 3px rgba(0,11,48,0.25);}</style>
    </div>
    <div id="rec752807230" class="r t-rec t-rec_pt_210 t-rec_pb_120 r_showed r_anim"
         style="padding-top:210px;padding-bottom:120px;background-image:linear-gradient(0.75turn,rgba(56,45,110,1) 0%,rgba(143,100,130,1) 50%,rgba(56,45,110,1) 100%); "
         data-record-type="490"
         data-bg-color="linear-gradient(0.75turn,rgba(56,45,110,1) 0%,rgba(143,100,130,1) 50%,rgba(56,45,110,1) 100%)">
        <!-- t490 -->
        <div class="t490">
            <div class="t-section__container t-container t-container_flex">
                <div class="t-col t-col_12 ">
                    <div class="t-section__title t-title t-title_xs t-align_center t-margin_auto" field="btitle">
                    Здравствуйте, ${user.name}!
                    </div>
                </div>
            </div>
            <style>.t-section__descr {max-width: 560px;}#rec752807230 .t-section__title {margin-bottom: 105px;}#rec752807230 .t-section__descr {}@media screen and (max-width: 960px) {#rec752807230 .t-section__title {margin-bottom: 45px;}}</style>
            <div class="t490__container t-card__container t-container">
                <div class="t-card__col t-card__col_withoutbtn t490__col t-col t-col_3 t-align_center t-item"
                     style="cursor: pointer;"><img class="t490__img t-img" src="/anastasia/style/7738925_wallet_money.svg"
                                                   imgfield="li_img__5139100788870" alt="">
                    <div class="t490__wrappercenter">
                        <div class="t-card__title t-heading t-heading_md" field="li_title__5139100788870"><a
                                href="/anastasia/user/accounts" class="t-card__link"
                                id="cardtitle1_752807230"><span style="color: rgb(255, 255, 255);">Счета</span></a>
                        </div>
                    </div>
                </div>
                <div class="t-card__col t-card__col_withoutbtn t490__col t-col t-col_3 t-align_center t-item"
                     style="cursor: pointer;"><img class="t490__img t-img" src="/anastasia/style/7217466_forex_trade_.svg"
                                                   imgfield="li_img__1716410714439" alt="">
                    <div class="t490__wrappercenter">
                        <div class="t-card__title t-heading t-heading_md" field="li_title__1716410714439"><a
                                href="/anastasia/market/MOEX/stocks?sort-by=TRADE_VOLUME&sort-order=desc" class="t-card__link"
                                id="cardtitle2_752807230">Список бумаг</a></div>
                    </div>
                </div>
                <div class="t-card__col t-card__col_withoutbtn t490__col t-col t-col_3 t-align_center t-item"
                     style="cursor: pointer;"><img class="t490__img t-img" src="/anastasia/style/7738942_transaction_.svg"
                                                   imgfield="li_img__5139100788871" alt="">
                    <div class="t490__wrappercenter">
                        <div class="t-card__title t-heading t-heading_md" field="li_title__5139100788871"><a
                                href="/anastasia/trade/orders" class="t-card__link"
                                id="cardtitle3_752807230"><span style="color: rgb(255, 255, 255);">Заявки</span></a>
                        </div>
                    </div>
                </div>
                <div class="t-card__col t-card__col_withoutbtn t490__col t-col t-col_3 t-align_center t-item"
                     style="cursor: pointer;"><img class="t490__img t-img" src="/anastasia/style/7397438_robot.svg"
                                                   imgfield="li_img__5139100788872" alt="">
                    <div class="t490__wrappercenter">
                        <div class="t-card__title t-heading t-heading_md" field="li_title__5139100788872"><a
                                href="/anastasia/smart/subscribe" class="t-card__link"
                                id="cardtitle4_752807230"><span style="color: rgb(255, 255, 255);">Смарт</span></a>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <script>t_onReady(function () {
            t_onFuncLoad('t490_init', function () {
                t490_init('752807230');
            });
        });</script>
    </div>
    <div id="rec752807016" class="r t-rec" style=" " data-animationappear="off" data-record-type="18"><!-- cover -->
        <div class="t-cover" id="recorddiv752807016" bgimgfield="img"
             style="background-image: url(&quot;https://static.tildacdn.info/tild3861-6236-4038-b964-313232653439/-/resize/20x/trade_app_background.png&quot;); height: 429.5px;">
            <div class="t-cover__carrier loaded" id="coverCarry752807016" data-content-cover-id="752807016"
                 data-content-cover-bg="https://static.tildacdn.info/tild3861-6236-4038-b964-313232653439/trade_app_background.png"
                 data-display-changed="true" data-content-cover-height="50vh" data-content-cover-parallax="fixed"
                 style="height: 429.5px; background-image: url(&quot;https://optim.tildacdn.pub/tild3861-6236-4038-b964-313232653439/-/format/webp/trade_app_background.png&quot;);"
                 itemscope="" itemtype="http://schema.org/ImageObject" data-content-cover-updated-height="429.5px">
                <meta itemprop="image"
                      content="https://static.tildacdn.info/tild3861-6236-4038-b964-313232653439/trade_app_background.png">
            </div>
            <div class="t-cover__filter"
                 style="height: 429.5px; background-image: -webkit-linear-gradient(top, rgba(0, 0, 0, 0.7), rgba(0, 0, 0, 0.7));"></div>
            <div class="t-container">
                <div class="t-col t-col_12 ">
                    <div class="t-cover__wrapper t-valign_middle" style="height: 429.5px;">
                        <div class="t001 t-align_center">
                            <div class="t001__wrapper" data-hook-content="covercontent"><span class="space"></span>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <style> #rec752807016 .t001__uptitle { text-transform: uppercase; }</style>
    </div>
</div>
<script type="text/javascript">if (! window.mainTracker) { window.mainTracker = 'tilda'; }
window.tildastatcookie='no';
setTimeout(function(){ (function (d, w, k, o, g) { var n=d.getElementsByTagName(o)[0],s=d.createElement(o),f=function(){n.parentNode.insertBefore(s,n);}; s.type = "text/javascript"; s.async = true; s.key = k; s.id = "tildastatscript"; s.src=g; if (w.opera=="[object Opera]") {d.addEventListener("DOMContentLoaded", f, false);} else { f(); } })(document, window, '9bbfc50c62aa9e581d78a7ed0c24bf3e','script','https://static.tildacdn.info/js/tilda-stat-1.0.min.js');
}, 2000); </script>
<script async="" type="text/javascript" src="/anastasia/style/tilda-performance-1.0.min.js"></script>
</body>
</html>