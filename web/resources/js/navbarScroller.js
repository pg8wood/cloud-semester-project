/* JavaScript from: http://jqueryfordesigners.com/fixed-floating-elements/ */

$(function () {
    var sidebar = $('.sidebarLeft');
    var top = sidebar.offset().top - parseFloat(sidebar.css('margin-top'));
  
    $(window).scroll(function (event) {
      var y = $(this).scrollTop();
      if (y >= top) {
        sidebar.addClass('fixed');
      } else {
        sidebar.removeClass('fixed');
      }
    });
});