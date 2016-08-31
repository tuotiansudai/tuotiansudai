require(['jquery'],function($){
	$(function() {
		var $taskBox=$('#taskFrame').find('.task-box'),
			$taskStatusMenu=$('#taskStatusMenu'),
			$buttonMore=$('.button-more');

			
		$('.notice-tip').on('click',function() {
            $taskBox.toggle();
            var $this=$(this);
            if($taskBox.is(':hidden')) {
                $this.find('i').addClass('fa-chevron-down').removeClass('fa-chevron-up');
            }
            else {
                $this.find('i').addClass('fa-chevron-up').removeClass('fa-chevron-down');
            }
        });

        //taskStatusMenu
        $taskStatusMenu.find('span').click(function(event) {
            event.preventDefault();
            var $this=$(this),
                index=$this.index();
            $this.addClass('active').siblings('span').removeClass('active');
            $('.task-status').eq(index).show().siblings('.task-status').hide();
        });

        $buttonMore.on('click',function(event) {
            event.preventDefault();
            var $this=$(this),
                $parentBox=$this.parents('.task-status');
            $this.toggleClass('open');
            if($this.hasClass('open')) {
                $parentBox.find('.border-box').show();
                $this.find('span').text('收起');
                $this.find('i').addClass('fa-chevron-circle-up').removeClass('fa-chevron-circle-down');
            }
            else {
                $parentBox.find('.border-box').hide();
                $parentBox.find('.border-box:lt(4)').show();
                $this.find('span').text('点击查看更多任务')
                $this.find('i').addClass('fa-chevron-circle-down').removeClass('fa-chevron-circle-up');
            }
        });
	});
})