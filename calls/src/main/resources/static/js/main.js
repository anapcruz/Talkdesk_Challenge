$(document).ready(function(){
    $('.nBtn').on('click',function (event){
        event.preventDefault();

        $('.myForm #callerNumber').val('');
        $('.myForm #calleeNumber').val('');
        $('.myForm #type').val('');
        $('.myForm #createCall').modal();
    });
});