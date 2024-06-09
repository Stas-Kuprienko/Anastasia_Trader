<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<sec:authorize access="isAuthenticated()">
  <c:redirect url = "http://localhost:8081/anastasia"/>
</sec:authorize>

<!DOCTYPE html>
<html lang="ru">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0"> <!--metatextblock--><title>Регистрация</title>
    <meta property="og:url" content="http://project9588603.tilda.ws/page49487079.html">
    <meta property="og:title" content="/signup">
    <meta property="og:description" content="">
    <meta property="og:type" content="website">
    <meta property="og:image"
          content="https://static.tildacdn.info/tild3965-3939-4637-b661-343966356332/-/resize/504x/trade_app_background.png">
    <link rel="canonical" href="http://project9588603.tilda.ws/page49487079.html"><!--/metatextblock-->
    <meta name="format-detection" content="telephone=no">
    <meta http-equiv="x-dns-prefetch-control" content="on">
    <link rel="dns-prefetch" href="https://ws.tildacdn.com/">
    <link rel="dns-prefetch" href="https://static.tildacdn.info/">
    <meta name="robots" content="nofollow">
    <link rel="icon" href="/anastasia/favicon1.svg" sizes="any" type="image/svg+xml">
    <!-- Assets -->
    <script type="text/javascript" async="" id="tildastatscript" src="/anastasia/style/tilda-stat-1.0.min.js"></script>
    <script src="/anastasia/style/tilda-fallback-1.0.min.js" async="" charset="utf-8"></script>
    <link rel="stylesheet" href="/anastasia/style/tilda-grid-3.0.min.css" type="text/css" media="all"
          onerror="this.loaderr=&#39;y&#39;;">
    <link rel="stylesheet" href="/anastasia/style/tilda-blocks-page49487079.min.css" type="text/css" media="all"
          onerror="this.loaderr=&#39;y&#39;;">
    <link rel="stylesheet" href="/anastasia/style/tilda-forms-1.0.min.css" type="text/css" media="all"
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
}
    </script>
    <script src="/anastasia/style/tilda-scripts-3.0.min.js" charset="utf-8" defer=""
            onerror="this.loaderr=&#39;y&#39;;"></script>
    <script src="/anastasia/style/tilda-blocks-page49487079.min.js" charset="utf-8" async=""
            onerror="this.loaderr=&#39;y&#39;;"></script>
    <script src="/anastasia/style/tilda-lazyload-1.0.min.js" charset="utf-8" async=""
            onerror="this.loaderr=&#39;y&#39;;"></script>
    <script src="/anastasia/style/tilda-forms-1.0.min.js" charset="utf-8" async=""
            onerror="this.loaderr=&#39;y&#39;;"></script>
    <script src="/anastasia/style/tilda-cover-1.0.min.js" charset="utf-8" async=""
            onerror="this.loaderr=&#39;y&#39;;"></script>
    <script src="/anastasia/style/tilda-menu-1.0.min.js" charset="utf-8" async=""
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
})();
    </script>
    <style type="text/css">
        @media screen and (min-width: 980px) {.t-records {opacity: 0;}.t-records_animated {-webkit-transition: opacity ease-in-out .2s;-moz-transition: opacity ease-in-out .2s;-o-transition: opacity ease-in-out .2s;transition: opacity ease-in-out .2s;}.t-records.t-records_visible {opacity: 1;}}
    </style>
</head>
<body class="t-body" style="margin:0;"><!--allrecords-->
<div id="allrecords" class="t-records t-records_animated t-records_visible" data-hook="blocks-collection-content-node"
     data-tilda-project-id="9588603" data-tilda-page-id="49487079"
     data-tilda-formskey="c760884931208f612fcce878e9588603" data-tilda-cookie="no" data-tilda-lazy="yes"
     data-tilda-root-zone="com">
    <div id="rec752799850" class="r t-rec t-rec_pt_120 t-rec_pb_60 r_showed r_anim"
         style="padding-top:120px;padding-bottom:60px;background-image:linear-gradient(0.75turn,rgba(56,45,110,1) 0%,rgba(143,100,130,1) 50%,rgba(56,45,110,1) 100%); "
         data-record-type="678"
         data-bg-color="linear-gradient(0.75turn,rgba(56,45,110,1) 0%,rgba(143,100,130,1) 50%,rgba(56,45,110,1) 100%)">
        <!-- t678 -->
        <div class="t678 ">
            <div class="t-container">
                <div class="t-col t-col_8 t-prefix_2">
                    <div>
                        <form id="form752799850" name="form752799850" role="form"
                              action="/anastasia/sign-up" method="POST"
                              data-formactiontype="0" data-inputbox=".t-input-group"
                              data-success-callback="t678_onSuccess"><!-- NO ONE SERVICES CONNECTED --> <input
                                type="hidden" name="tildaspec-formname" tabindex="-1" value="Авторизация">
                            <div class="js-successbox t-form__successbox t-text t-text_md" aria-live="polite"
                                 style="display:none;" data-success-message="Выполняется регистрация"></div>
                            <div class="t-form__inputsbox t-form__inputsbox_vertical-form t-form__inputsbox_inrow">
                                <div class="t-input-group t-input-group_nm " data-input-lid="1716496121972"
                                     data-field-type="nm"><label for="input_1716496121972"
                                                                 class="t-input-title t-descr t-descr_md"
                                                                 id="field-title_1716496121972"
                                                                 data-redactor-toolbar="no"
                                                                 field="li_title__1716496121972" style="color:;">Ваше
                                    имя</label>
                                    <div class="t-input-block "><input type="text" autocomplete="name" name="name"
                                                                       id="input_1716496121972"
                                                                       class="t-input js-tilda-rule " value=""
                                                                       aria-describedby="error_1716496121972"
                                                                       style="color:#000000;border:1px solid #000000;">
                                        <div class="t-input-error" aria-live="polite" id="error_1716496121972"></div>
                                    </div>
                                </div>
                                <div class="t-input-group t-input-group_em " data-input-lid="5527479220180"
                                     data-field-type="em"><label for="input_5527479220180"
                                                                 class="t-input-title t-descr t-descr_md"
                                                                 id="field-title_5527479220180"
                                                                 data-redactor-toolbar="no"
                                                                 field="li_title__5527479220180"
                                                                 style="color:;">Логин</label>
                                    <div class="t-input-block "><input type="email" autocomplete="email" name="email"
                                                                       id="input_5527479220180"
                                                                       class="t-input js-tilda-rule " value=""
                                                                       placeholder="mycompany@co.com" data-tilda-req="1"
                                                                       aria-required="true"
                                                                       aria-describedby="error_5527479220180"
                                                                       style="color:#000000;border:1px solid #000000;">
                                        <div class="t-input-error" aria-live="polite" id="error_5527479220180"></div>
                                        <div data-lastpass-icon-root=""
                                             style="position: relative !important; height: 0px !important; width: 0px !important; float: left !important;">
                                            <template shadowrootmode="closed">
                                                <svg width="24" height="24" viewBox="0 0 24 24" fill="none"
                                                     xmlns="http://www.w3.org/2000/svg" data-lastpass-icon="true"
                                                     style="position: absolute; cursor: pointer; height: 22px; max-height: 22px; width: 22px; max-width: 22px; top: 19px; left: 713px; z-index: auto; color: rgb(186, 192, 202);">
                                                    <rect x="0.680176" y="0.763062" width="22.6392" height="22.4737"
                                                          rx="4" fill="currentColor"></rect>
                                                    <path fill-rule="evenodd" clip-rule="evenodd"
                                                          d="M19.7935 7.9516C19.7935 7.64414 20.0427 7.3949 20.3502 7.3949C20.6576 7.3949 20.9069 7.64414 20.9069 7.9516V16.0487C20.9069 16.3562 20.6576 16.6054 20.3502 16.6054C20.0427 16.6054 19.7935 16.3562 19.7935 16.0487V7.9516Z"
                                                          fill="white"></path>
                                                    <path fill-rule="evenodd" clip-rule="evenodd"
                                                          d="M4.76288 13.6577C5.68525 13.6577 6.43298 12.9154 6.43298 11.9998C6.43298 11.0842 5.68525 10.3419 4.76288 10.3419C3.8405 10.3419 3.09277 11.0842 3.09277 11.9998C3.09277 12.9154 3.8405 13.6577 4.76288 13.6577Z"
                                                          fill="white"></path>
                                                    <path fill-rule="evenodd" clip-rule="evenodd"
                                                          d="M10.3298 13.6577C11.2521 13.6577 11.9999 12.9154 11.9999 11.9998C11.9999 11.0842 11.2521 10.3419 10.3298 10.3419C9.4074 10.3419 8.65967 11.0842 8.65967 11.9998C8.65967 12.9154 9.4074 13.6577 10.3298 13.6577Z"
                                                          fill="white"></path>
                                                    <path fill-rule="evenodd" clip-rule="evenodd"
                                                          d="M15.8964 13.6577C16.8188 13.6577 17.5665 12.9154 17.5665 11.9998C17.5665 11.0842 16.8188 10.3419 15.8964 10.3419C14.974 10.3419 14.2263 11.0842 14.2263 11.9998C14.2263 12.9154 14.974 13.6577 15.8964 13.6577Z"
                                                          fill="white"></path>
                                                </svg>
                                            </template>
                                        </div>
                                    </div>
                                </div>
                                <div class="t-input-group t-input-group_ps " data-input-lid="5527479220182"
                                     data-field-type="ps"><label for="input_5527479220182"
                                                                 class="t-input-title t-descr t-descr_md"
                                                                 id="field-title_5527479220182"
                                                                 data-redactor-toolbar="no"
                                                                 field="li_title__5527479220182"
                                                                 style="color:;">Пароль</label>
                                    <div class="t-input-block "><input type="password" autocomplete="password" name="password"
                                                                       id="input_5527479220182"
                                                                       class="t-input js-tilda-rule " value=""
                                                                       placeholder="password1234" data-tilda-req="1"
                                                                       aria-required="true"
                                                                       aria-describedby="error_5527479220182"
                                                                       style="color:#000000;border:1px solid #000000;">
                                        <div class="t-input-error" aria-live="polite" id="error_5527479220182"></div>
                                    </div>
                                </div>
                                <div class="t-form__errorbox-middle"><!--noindex-->
                                    <div class="js-errorbox-all t-form__errorbox-wrapper" style="display:none;"
                                         data-nosnippet="" tabindex="-1" aria-label="Ошибки при заполнении формы">
                                        <ul role="list" class="t-form__errorbox-text t-text t-text_md">
                                            <li class="t-form__errorbox-item js-rule-error js-rule-error-all"></li>
                                            <li class="t-form__errorbox-item js-rule-error js-rule-error-req"></li>
                                            <li class="t-form__errorbox-item js-rule-error js-rule-error-email"></li>
                                            <li class="t-form__errorbox-item js-rule-error js-rule-error-name"></li>
                                            <li class="t-form__errorbox-item js-rule-error js-rule-error-phone"></li>
                                            <li class="t-form__errorbox-item js-rule-error js-rule-error-minlength"></li>
                                            <li class="t-form__errorbox-item js-rule-error js-rule-error-string"></li>
                                        </ul>
                                    </div><!--/noindex--> </div>
                                <div class="t-form__submit">
                                    <button type="submit" class="t-submit"
                                            style="color:#ffffff;border:2px solid #1e1e1e;background-image:linear-gradient(0.75turn,rgba(56,45,110,1) 0%,rgba(143,100,130,1) 50%,rgba(56,45,110,1) 100%);border-radius:0px; -moz-border-radius:0px; -webkit-border-radius:0px;box-shadow:0px 10px 20px rgba(0,11,48,0.25);"
                                            data-field="buttontitle" data-buttonfieldset="button">Регистрация
                                    </button>
                                </div>
                            </div>
                            <div class="t-form__errorbox-bottom"><!--noindex-->
                                <div class="js-errorbox-all t-form__errorbox-wrapper" style="display:none;"
                                     data-nosnippet="" tabindex="-1" aria-label="Ошибки при заполнении формы">
                                    <ul role="list" class="t-form__errorbox-text t-text t-text_md">
                                        <li class="t-form__errorbox-item js-rule-error js-rule-error-all"></li>
                                        <li class="t-form__errorbox-item js-rule-error js-rule-error-req"></li>
                                        <li class="t-form__errorbox-item js-rule-error js-rule-error-email"></li>
                                        <li class="t-form__errorbox-item js-rule-error js-rule-error-name"></li>
                                        <li class="t-form__errorbox-item js-rule-error js-rule-error-phone"></li>
                                        <li class="t-form__errorbox-item js-rule-error js-rule-error-minlength"></li>
                                        <li class="t-form__errorbox-item js-rule-error js-rule-error-string"></li>
                                    </ul>
                                </div><!--/noindex--> </div>
                            <div style="position: absolute; left: -5000px; bottom: 0; display: none;"><input type="text"
                                                                                                             name="form-spec-comments"
                                                                                                             value="Its good"
                                                                                                             class="js-form-spec-comments"
                                                                                                             tabindex="-1">
                            </div>
                        </form>
                        <style>#rec752799850 input::-webkit-input-placeholder {color:#000000; opacity: 0.5;}
#rec752799850 input::-moz-placeholder {color:#000000; opacity: 0.5;}
#rec752799850 input:-moz-placeholder {color:#000000; opacity: 0.5;}
#rec752799850 input:-ms-input-placeholder {color:#000000; opacity: 0.5;}
#rec752799850 textarea::-webkit-input-placeholder {color:#000000; opacity: 0.5;}
#rec752799850 textarea::-moz-placeholder {color:#000000; opacity: 0.5;}
#rec752799850 textarea:-moz-placeholder {color:#000000; opacity: 0.5;}
#rec752799850 textarea:-ms-input-placeholder {color:#000000; opacity: 0.5;}
                        </style>
                    </div>
                </div>
            </div>
        </div>
        <style>
            #rec752799850 .t-submit {position: relative;overflow: hidden;z-index: 1;}#rec752799850 .t-submit::after {content: '';position: absolute;top: 0;left: 0;right: 0;bottom: 0;z-index: -1;width: 100%;height: 100%;opacity: 0;transition: opacity 0.2s ease-in-out;background-image: linear-gradient(0.75turn,rgba(63,50,123,1) 0%,rgba(153,106,139,1) 50%,rgba(63,50,123,1) 100%);}@media (hover: hover), (min-width: 0\0) {#rec752799850 .t-submit:hover::after {opacity: 1;}#rec752799850 .t-submit:focus-visible::after {opacity: 1;}}@media (hover: hover), (min-width: 0\0) {#rec752799850 .t-submit:hover {animation-name: rec_752799850_btnanim;animation-duration: 0s;animation-fill-mode: forwards;animation-delay: 0.2s;animation-timing-function: linear;}#rec752799850 .t-submit:focus-visible {animation-name: rec_752799850_btnanim;animation-duration: 0s;animation-fill-mode: forwards;animation-delay: 0.2s;animation-timing-function: linear;}}@keyframes rec_752799850_btnanim {to {background-image: none;background-color: transparent;}}@media (hover: hover), (min-width: 0\0) {#rec752799850 .t-submit:hover {box-shadow: 0px 2px 3px rgba(0,11,48,0.25) !important;}#rec752799850 .t-submit:focus-visible {box-shadow: 0px 2px 3px rgba(0,11,48,0.25) !important;}}
        </style>
    </div>
    <div id="rec752799851" class="r t-rec t-rec_pt_0 t-rec_pb_0" style="padding-top:0px;padding-bottom:0px; "
         data-animationappear="off" data-record-type="18"><!-- cover -->
        <div class="t-cover" id="recorddiv752799851" bgimgfield="img"
             style="background-image: url(&quot;https://static.tildacdn.info/tild3965-3939-4637-b661-343966356332/-/resize/20x/trade_app_background.png&quot;); height: 231.93px;">
            <div class="t-cover__carrier" id="coverCarry752799851" data-content-cover-id="752799851"
                 data-content-cover-bg="https://static.tildacdn.info/tild3965-3939-4637-b661-343966356332/trade_app_background.png"
                 data-display-changed="true" data-content-cover-height="27vh" data-content-cover-parallax="fixed"
                 style="height: 231.93px; background-image: url(&quot;https://static3.tildacdn.com/tild3965-3939-4637-b661-343966356332/trade_app_background.png&quot;);"
                 itemscope="" itemtype="http://schema.org/ImageObject" data-content-cover-updated-height="231.93px">
                <meta itemprop="image"
                      content="https://static.tildacdn.info/tild3965-3939-4637-b661-343966356332/trade_app_background.png">
            </div>
            <div class="t-cover__filter"
                 style="height: 231.93px; background-image: -webkit-linear-gradient(top, rgba(0, 0, 0, 0.7), rgba(0, 0, 0, 0.7));"></div>
            <div class="t-container">
                <div class="t-col t-col_12 ">
                    <div class="t-cover__wrapper t-valign_bottom" style="height: 231.93px;">
                        <div class="t001 t-align_center">
                            <div class="t001__wrapper" data-hook-content="covercontent"><span class="space"></span>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <style> #rec752799851 .t001__uptitle { text-transform: uppercase; }</style>
    </div>
    <div id="rec756861802" class="r t-rec t-rec_pb_0 r_showed r_anim"
         style="padding-bottom:0px;background-image:linear-gradient(0.75turn,rgba(56,45,110,1) 0%,rgba(143,100,130,1) 50%,rgba(56,45,110,1) 100%); "
         data-record-type="456"
         data-bg-color="linear-gradient(0.75turn,rgba(56,45,110,1) 0%,rgba(143,100,130,1) 50%,rgba(56,45,110,1) 100%)">
        <!-- T456 -->
        <div id="nav756861802marker"></div>
        <div class="tmenu-mobile tmenu-mobile_positionfixed">
            <div class="tmenu-mobile__container">
                <div class="tmenu-mobile__burgerlogo"><a href="/anastasia/"><img
                        src="/anastasia/style/home_circle_outline_.svg" class="tmenu-mobile__imglogo" imgfield="img"
                        style="max-width: 70px; width: 70px;" alt="&amp;lt;&amp;lt; Домашняя страница"></a></div>
                <button type="button" class="t-menuburger t-menuburger_first t-menuburger__small"
                        aria-label="Навигационное меню" aria-expanded="false"><span
                        style="background-color:#fff;"></span><span style="background-color:#fff;"></span><span
                        style="background-color:#fff;"></span><span style="background-color:#fff;"></span></button>
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
t_onFuncLoad('t_menuburger_init', function(){t_menuburger_init('756861802');});
});
                </script>
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
}
                </style>
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
#rec756861802 .tmenu-mobile {
background-color: #8f6482;
}
#rec756861802 .tmenu-mobile__menucontent_fixed {
position: fixed;
top: 64px;
}
#rec756861802 .t-menuburger {
-webkit-order: 1;
-ms-flex-order: 1;
order: 1;
}
}
        </style>
        <style> #rec756861802 .tmenu-mobile__burgerlogo a { font-size: 10px; color: #ffffff; }</style>
        <style> #rec756861802 .tmenu-mobile__burgerlogo__title { font-size: 10px; color: #ffffff; }</style>
        <div id="nav756861802"
             class="t456 t456__positionfixed tmenu-mobile__menucontent_hidden tmenu-mobile__menucontent_fixed" style=" "
             data-bgcolor-hex="" data-bgcolor-rgba="" data-navmarker="nav756861802marker" data-appearoffset=""
             data-bgopacity-two="" data-menushadow="" data-menushadow-css="" data-bgopacity="1"
             data-menu-items-align="right" data-menu="yes">
            <div class="t456__maincontainer " style="">
                <div class="t456__leftwrapper" style="min-width:70px;width:70px;">
                    <div class="t456__logowrapper" style="display: block;"><a
                            href="/anastasia/"><img class="t456__imglogo t456__imglogomobile"
                                                                        src="/anastasia/style/home_circle_outline_.svg"
                                                                        imgfield="img"
                                                                        style="max-width: 70px; width: 70px;"
                                                                        alt="&amp;lt;&amp;lt; Домашняя страница"></a>
                    </div>
                </div>
                <nav class="t456__rightwrapper t456__menualign_right" style="">
                    <ul role="list" class="t456__list t-menu__list">
                        <li class="t456__list_item" style="padding:0 0 0 15px 0 0;"><a class="t-menu__link-item"
                                                                                       href="/anastasia/login"
                                                                                       data-menu-submenu-hook=""
                                                                                       data-menu-item-number="1">Войти</a>
                        </li>
                    </ul>
                </nav>
            </div>
        </div>
        <style>@media screen and (max-width: 980px) {
#rec756861802 .t456__logowrapper{
display: none!important;
}
#rec756861802 .t456__mobile-text{
display: none;
}
#rec756861802 .t456__imglogo{
padding: 0;
}
}
        </style>
        <style>@media screen and (max-width: 980px) {
#rec756861802 .t456__leftcontainer{
padding: 20px;
}
}
@media screen and (max-width: 980px) {
#rec756861802 .t456__imglogo{
padding: 20px 0;
}
}
        </style>
        <script type="text/javascript">t_onReady(function() {
t_onFuncLoad('t_menu__highlightActiveLinks', function () {
t_menu__highlightActiveLinks('.t456__list_item a');
});
t_onFuncLoad('t_menu__findAnchorLinks', function () {
t_menu__findAnchorLinks('756861802', '.t456__list_item a');
});
});
t_onReady(function () {
t_onFuncLoad('t_menu__setBGcolor', function () {
t_menu__setBGcolor('756861802', '.t456');
window.addEventListener('resize', function () {
t_menu__setBGcolor('756861802', '.t456');
});
});
t_onFuncLoad('t_menu__interactFromKeyboard', function () {
t_menu__interactFromKeyboard('756861802');
});
});
        </script>
        <script type="text/javascript">t_onReady(function () {
t_onFuncLoad('t_menu__createMobileMenu', function () {
t_menu__createMobileMenu('756861802', '.t456');
});
});
        </script>
        <style>#rec756861802 .t-menu__link-item{
-webkit-transition: color 0.3s ease-in-out, opacity 0.3s ease-in-out;
transition: color 0.3s ease-in-out, opacity 0.3s ease-in-out;
}
#rec756861802 .t-menu__link-item:not(.t-active):not(.tooltipstered):hover {
color: #ddcee4 !important; }
#rec756861802 .t-menu__link-item:not(.t-active):not(.tooltipstered):focus-visible{
color: #ddcee4 !important; }
@supports (overflow:-webkit-marquee) and (justify-content:inherit)
{
#rec756861802 .t-menu__link-item,
#rec756861802 .t-menu__link-item.t-active {
opacity: 1 !important;
}
}
        </style>
        <style>
            #rec756861802 .t456__logowrapper a { font-size: 10px; color: #ffffff; } #rec756861802 a.t-menu__link-item { font-size: 20px; color: #ffffff; font-weight: 600; text-transform: uppercase; }
        </style>
        <style> #rec756861802 .t456__logo { font-size: 10px; color: #ffffff; }</style>
    </div>
</div>
<div data-lastpass-root=""
     style="position: absolute !important; top: 0px !important; left: 0px !important; height: 0px !important; width: 0px !important;">
    <template shadowrootmode="closed">
        <div style="position: absolute; height: 100vh; width: 100vw; z-index: 2147483647; border-radius: 4px; top: 0px; left: 0px; max-height: 0px; max-width: 280px; min-width: auto;">
            <iframe data-lastpass-infield="true" allow="clipboard-write"
                    src="chrome-extension://hdokiejnpimakedhajhdlcegeplioahd/webclient-infield.html"
                    style="border: none; height: 100%; width: 100%;"></iframe>
        </div>
    </template>
</div>
<script async="" type="text/javascript" src="/anastasia/style/tilda-performance-1.0.min.js"></script>
<script src="/anastasia/style/tilda-errors-1.0.min.js" type="text/javascript" async=""></script>
<script src="/anastasia/style/tilda-errors-1.0.min.js" async=""></script>
</body>
</html>