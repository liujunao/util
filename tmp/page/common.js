var common = {};

//ajax 请求
common.ajax = function (url, data, async, type, dataType, successfn, errorfn) {
    async=(async==null || async=="" || typeof (async)=="undefined") ? "true" : async;
    type=(type==null || type=="" || typeof (type)=="undefined") ? "post" : type;
    dataType=(dataType==null || dataType=="" || typeof (dataType)=="undefined") ? "json" : dataType;
    data=(data==null || data=="" || typeof (data)=="undefined") ? { "date": new Date().getTime() } : data;
    $.ajax({
        type: type,
        async: async,
        data: data,
        url: url,
        dataType: dataType,
        success: function (d) {
            successfn(d);
        },
        error: function (e) {
            errorfn(e);
        }
    });
};

//获取选中的checkbox 值
common.getCbValue = function (item) {
    var s = "";
    $(item).each(function () {
        if ($(this).is(":checked")) {
            s = s + $(this).val() + ",";
        }
    })
    return s;
}


var ControlBuild = {};
//分页控件 
//可以反复执行
//endFun($page, page, pagesize);
ControlBuild.PageBuild = function (endFun) {
    jQuery("control_page").hide();
    jQuery("control_page").each(function () {
        var $this = jQuery(this);
        var $page = $this.next();
        if ($page.hasClass("pagination"))
            $page.remove();
        var count = $this.attr("count");
        var otherpara = $this.attr("otherpara");
        var topage = $this.attr("topage");
        var p = $this.attr("page");
        var json = { "count": parseInt(count), "otherpara": otherpara, "topage": topage, "page": parseInt(p) };
        var pagesize = $this.attr("pagesize");
        if (pagesize)
            json.pagesize = pagesize;
        $this.after(ControlBuild.PageHtml(json));
        if (endFun) {
            endFun($this.next(), p, pagesize, $this);
        }
    });
};
//json = { "count": count, "otherpara": otherpara, "topage": topage, "page": p, "pagesize":20 };
ControlBuild.PageHtml = function (json) {
    function buildPageLi(abled, li_page, currpage, urlHead, pagesize) {
        var classActive = li_page == currpage ? " active" : "";
        var href = "javascript:void(0);";
        if (abled)
            href = urlHead + "&page=" + li_page + "&pagesize=" + pagesize;
        return "<li class=\"page" + classActive + "\"><a href=\"" + href + "\" page=\"" + li_page + "\">" + li_page + "</a></li>";
    }
    function buildBtnLi(abled, txt, li_page, currpage, urlHead, pagesize) {
        var href = "javascript:void(0);";
        var classdisabled = "disabled";
        if (abled) {
            classdisabled = "";
            href = urlHead + "&page=" + li_page + "&pagesize=" + pagesize;
        }
        return "<li class=\"" + classdisabled + "\"><a href=\"" + href + "\" page=\"" + li_page + "\" title=\"" + txt + "\">" + txt + "</a></li>";
    }
    if (!json.pagesize)
        json.pagesize = 20;
    var pagenum = 1;
    if (json.count > 0) {
        pagenum = parseInt((json.count - 1) / json.pagesize) + 1;
    }
    var pagehtml = "";
    var preAbled = json.page > 1;
    var nextAbled = json.page < pagenum;
    var urlHead = json.topage + "?testpara=0";
    if (json.otherpara && json.otherpara.length > 2)
        urlHead += "&" + decodeURIComponent(json.otherpara);
    pagehtml += "<div class=\"pagination pagination-centered\" id=\"changePage\">";
    pagehtml += "<ul>";
    pagehtml += buildBtnLi(preAbled, "首页", 1, json.page, urlHead, json.pagesize);
    pagehtml += buildBtnLi(preAbled, "上一页", json.page - 1, json.page, urlHead, json.pagesize);
    for (var i = 1; i <= pagenum; i++) {
        var iAbled = json.page != i;
        pagehtml += buildPageLi(iAbled, i, json.page, urlHead, json.pagesize);
    }
    pagehtml += buildBtnLi(nextAbled, "下一页", json.page + 1, json.page, urlHead, json.pagesize);
    pagehtml += buildBtnLi(nextAbled, "末页", pagenum, json.page, urlHead, json.pagesize);
    pagehtml += "</ul>";
    pagehtml += "</div>";
    return pagehtml;
};
//分页控件 end