$(function () {
	//发送私信框
   $('.l-reply,.send').click(function () {
      var username = '';
      username = $(this).attr('rel');

   	var letterLeft = ($(window).width() - $('#letter').width()) / 2;
	 	var letterTop = $(document).scrollTop() + ($(window).height() - $('#letter').height()) / 2;
		var obj = $('#letter').show().css({
	 		'left' : letterLeft,
	 		'top' : letterTop
	 	});

      obj.find('input[name=toName]').val(username);
      obj.find('textarea').focus();
		createBg('letter-bg');
		drag(obj, obj.find('.letter_head'));
   });
   //关闭
   $('.letter-cencle').click(function () {
   		$('#letter').hide();
   		$('#letter-bg').remove();
   });

   /**
    * 删除私信
    */
   $('.del-letter').click(function () {
      var isDel = confirm('确定删除该私信？');
      var lid = $(this).attr('lid');
      var obj = $(this).parents('dl');

      if (isDel) {
         $.post(delLetter, {lid : lid}, function (data) {
            if (data) {
               obj.slideUp('slow', function () {
                  obj.remove();
               });
            } else {
               alert('删除失败重请试...');
            }
         }, 'json');
      }
   })
})