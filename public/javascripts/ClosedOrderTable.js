jQuery(document).ready(function($) {
    $("#closed_orders_table tr").each(function(i, e){
        $(e).children('td:not(:last)').click(function()
        {
            window.document.location = $(this).closest("tr").data("href");
        });
    });
});
