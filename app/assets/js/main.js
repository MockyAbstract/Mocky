
$(function() {

    $("#statusCode").select2({
        createSearchChoice: function(term, data) { if ($(data).filter(function() { return this.text.localeCompare(term)===0; }).length===0) {return {id:term, text:term};} },
        multiple: false,
        width: "element",
        data: _(referentials.codes).map(function(name, code) { return { id: code, text: code + " " + name}; }).value()
    })
    $("#statusCode").select2('data', {id: 200, text:"200 OK"})

    $("#charset").select2({
        createSearchChoice: function(term, data) { if ($(data).filter(function() { return this.text.localeCompare(term)===0; }).length===0) {return {id:term, text:term};} },
        multiple: false,
        width: "element",
        data: _(referentials.charsets).map(function(charset) { return { id: charset, text: charset}; }).value()
    })
    $("#charset").select2('data', { id: "UTF-8", text: "UTF-8" })

    $("#contentType").select2({
        createSearchChoice: function(term, data) { if ($(data).filter(function() { return this.text.localeCompare(term)===0; }).length===0) {return {id:term, text:term};} },
        multiple: false,
        width: "element",
        data: _(referentials.contentTypes).map(function(e, f) { return f; }).sortBy().map(function(e) { return { id: e, text: e}; }).value()
    })

    $("#contentType").select2('data', { id: "application/json", text: "application/json" })

    $("#new-mocky-form").on('submit', function(e) {
        e.preventDefault();
        _gaq.push(['_trackEvent', 'Mock', 'create', 'mock requested', , false]);
        $("#send-btn").text(I18n("btn.wait")).attr("disabled", "disabled")
        var body = $(this).serializeArray().concat({name: "body", value: editor.getSession().getValue()})
        $.post("/", body)
            .done(function(data) {
                _gaq.push(['_trackEvent', 'Mock', 'success', 'mock created with success', , false]);
                $("#feedback p").html('<strong>'+I18n("alert.linkReady")+'</strong> <a href="'+data.url+'" target="blank">'+data.url+'</a>')
                $("#feedback").addClass("alert-success").removeClass("alert-error");
            })
            .fail(function() {
                _gaq.push(['_trackEvent', 'Mock', 'failed', 'error when creating mock', , false]);
                $("#feedback p").html(I18n("error.retry"))
                $("#feedback").addClass("alert-error").removeClass("alert-success");
            })
            .always(function() {
                $("#send-btn").text(I18n("btn.generate")).attr("disabled", null)
                $("#feedback").show()
                $(".confidential-alert").hide()
            })
    })

    $("#contentType").on("change", function() {
        var ct = $(this).val()
        var lang = referentials.contentTypes[ct]
        if (lang != undefined) {
            changeEditorLanguage(lang)
        }
    })

    $("#statusCode").on("change", function() {
        var code = $(this).val()
        if (code.length > 0 && code[0]=="3") {
            $("#location-block").show();
        } else {
            $("#location-block").hide().find("input").val("")
        }
    })

    $(".btn-advanced-mode").on("click", function(e) {
        e.preventDefault()
        var $block = $(".advanced-block")
        if ($block.is(":visible")) {
            $block.hide();
            $(".btn-advanced-mode").text(I18n("btn.advanced"))
        } else {
            $block.show();
            $(".btn-advanced-mode").text(I18n("btn.basic"))
        }
    })

    $("#advanced-block-inner").on("click", ".btn-add-header", function(e) {
        e.preventDefault()
        var $context = $("#advanced-block-inner")
        var origin = $context.find(".clone")
        var clone = origin.clone()
        clone.find("input[type=text]").each(function() { $(this).val("") })
        $context.append(clone)
        origin.removeClass("clone")
        $(this).remove()
    })

    var editor = initNewEditor("editor");

    function initNewEditor(id) {
        var editor = ace.edit(id);
        editor.setTheme("ace/theme/tomorrow");
        editor.setHighlightActiveLine(false);
        editor.setShowPrintMargin(false);
        editor.renderer.setShowGutter(false);
        editor.getSession().setMode("ace/mode/json");
        return editor;
    }

    function changeEditorLanguage(lang) {
        editor.getSession().setMode("ace/mode/"+lang)
    }

});
