/**
 * Created by Firstvan on 2016. 02. 23..
 */

jQuery(document).ready(function($) {
    $("#users-table").find("tr").each(function(i, e){
        $(e).children('td:not(:nth-last-child(-n+2))').click(function()
        {
            window.document.location = $(this).closest("tr").data("href");
        });
    });
});
