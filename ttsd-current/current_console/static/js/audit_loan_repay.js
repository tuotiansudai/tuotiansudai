(function ($) {

     $('.loan-repay-pass').on('click', function (event) {
         var self = $(this);
         $('#pass').modal({});
             $('.pass-no').on('click',function (event) {
                 $('#pass').modal('hide');
             });
             $('.pass-true').on('click', function (event) {
                  $.ajax({
                      url : '/audit-loan-repay/pass/' + self.data('loan-repay-id'),
                      type : 'PUT',
                      dataType :'json',
                      contentType: 'application/json',
                      data:JSON.stringify({
                          'loan_id': $('#loan_id').val(),
                          'task_id': $('#task_id').val()
                          })
                  })
                      .done(function (data) {
                          if(data.message == 'success'){
                              layer.msg('审核成功');
                              $('#pass').modal('hide');
                              $('#audit-operation').hide();
                              $('#resultApproved').show();
                          }else{
                              layer.msg('审核失败');
                          }

                      })
                      .fail(function (data) {
                          layer.msg('审核失败,'+JSON.parse(data.responseText).message)
                      });
             });
     });

     $('.loan-repay-reject').on('click', function (event) {
         var self = $(this);
         $('#reject').modal({});
             $('.reject-no').on('click',function (event) {
                 $('#reject').modal('hide');
             });
             $('.reject-true').on('click', function (event) {
                  $.ajax({
                      url : '/audit-loan-repay/reject/' + self.data('loan-repay-id'),
                      type : 'PUT',
                      dataType :'json',
                      contentType: 'application/json',
                      data:JSON.stringify({
                          'loan_id': $('#loan_id').val(),
                          'task_id': $('#task_id').val()
                          })
                  })
                      .done(function (data) {
                          if(data.message == 'success'){
                              layer.msg('驳回成功')
                              $('#reject').modal('hide');
                              $('#audit-operation').hide();
                              $('#resultReject').show();
                          }else{
                              layer.msg('驳回失败')
                          }

                      })
                      .fail(function (data) {
                          layer.msg('驳回失败,'+JSON.parse(data.responseText).message)
                      });
             });
     });
 })(jQuery);
