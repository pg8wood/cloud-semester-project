function handleSubmit(args, fromDialog, toDialog) {
    var jqDialog = jQuery('#' + fromDialog);
    if (args.validationFailed) {
        jqDialog.effect('shake', {times: 3}, 100);
    } else {
        //PF(fromDialog).hide();
        PF(toDialog).show();
    }
}