jQuery(document).ready(function($) {
    $("#closed_orders_table tr").each(function(i, e){
        $(e).children('td:not(:last)').click(function()
        {
            console.log("fut");
            window.document.location = $(this).closest("tr").data("href");
        });
    });
});
