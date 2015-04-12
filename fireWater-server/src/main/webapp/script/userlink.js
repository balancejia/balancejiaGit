(function($) {
    /**
     * Creates a new hover popup
     *
     * @param items jQuery object - the items that trigger the display of this popup when the user mouses over.
     * @param identifier A unique identifier for this popup. This should be unique across all popups on the page and a
     *                   valid CSS class.
     * @param url The URL to retrieve popup contents.
     * @param options Custom options to change default behaviour. See AJS.contentHover.opts for default values and valid options.
     *
     * @return jQuery object - the popup that was created
     */
    $.contentHover = function(items, identifier, url, options) {
        var opts = $.extend(false, $.contentHover.opts, options);
        var contents,
            mousePosition,
            popup = $("#content-hover-" + identifier);
        if (!popup.length) {
            $(opts.container).append($('<div id="content-hover-' + identifier + '" class="ajs-content-hover">' +
                    '<div class="contents"></div></div>'));
            popup = $("#content-hover-" + identifier);
            popup.css({
            	"position":"absolute",
            	"display":"none",
            	"z-index":"3100",
            	"text-align":"left"
            });
            contents = popup.find(".contents");
           contents.css("width","auto")
            .mouseover(function() {
                clearTimeout(p.hideDelayTimer);
                popup.unbind("mouseover");
            }).mouseout(function() {
                hidePopup();
            });
        } else {
            contents = popup.find(".contents");
        }

        var p = popup[0];

        var showPopup = function() {
            if (popup.is(":visible")) {
                return;
            }

            p.showTimer = setTimeout(function() {
                if (!p.contentLoaded || !p.shouldShow) {
                    return;
                }
                p.beingShown = true;
                var $window = $(window),
                    posx = mousePosition.x - 3,
                    posy = mousePosition.y + 15;

                if (posx + opts.width + 30 > $window.width()) {
                    popup.css({
                        right: "20px",
                        left: "auto"
                    });
                }
                else {
                    popup.css({
                        left: posx + "px",
                        right: "auto"
                    });
                }
                var bottomOfViewablePage = (window.pageYOffset || document.documentElement.scrollTop) + $window.height();
                if ((posy + popup.height()) > bottomOfViewablePage) {
                    posy = bottomOfViewablePage - popup.height() - 5;
                    popup.mouseover(function() {
                        clearTimeout(p.hideDelayTimer);
                    }).mouseout(function() {
                        hidePopup();
                    });
                }
                popup.css({
                    top: posy + "px"
                });
                var shadow = $("#content-hover-shadow").appendTo(popup).show();
                // reset position of popup box
                popup.fadeIn(opts.fadeTime, function() {
                    // once the animation is complete, set the tracker variables
                    p.beingShown = false;
                });

                shadow.css({
                    width: contents.outerWidth() + 32 + "px",
                    height: contents.outerHeight() + 25 + "px"
                });
                $(".b", shadow).css("width", contents.outerWidth() - 26 + "px");
                $(".l, .r", shadow).css("height", contents.outerHeight() - 21 + "px");
            }, opts.showDelay);
        };

        var hidePopup = function() {
            p.beingShown = false;
            p.shouldShow = false;
            clearTimeout(p.hideDelayTimer);
            clearTimeout(p.showTimer);
            clearTimeout(p.loadTimer);
            p.contentLoading = false;
            p.shouldLoadContent = false;
            // store the timer so that it can be cleared in the mouseover if required
            p.hideDelayTimer = setTimeout(function() {
                popup.fadeOut(opts.fadeTime);
            }, opts.hideDelay);
        };

        $(items).mousemove(function(e) {
            mousePosition = { x: e.pageX, y: e.pageY };
            
            if (!p.beingShown) {
                clearTimeout(p.showTimer);
            }
            p.shouldShow = true;
            // lazy load popup contents
            if (!p.contentLoaded) {
                if (p.contentLoading) {
                    // If the mouse has moved more than the threshold don't load the contents
                    if (p.shouldLoadContent) {
                        var distance = (mousePosition.x - p.initialMousePosition.x)*(mousePosition.x - p.initialMousePosition.x)
                                + (mousePosition.y - p.initialMousePosition.y) * (mousePosition.y - p.initialMousePosition.y);
                        if (distance > (opts.mouseMoveThreshold * opts.mouseMoveThreshold)) {
                            p.contentLoading = false;
                            p.shouldLoadContent = false;
                            clearTimeout(p.loadTimer);
                            return;
                        }
                    }
                } else {
                    // Save the position the mouse started from
                    p.initialMousePosition = mousePosition;
                    p.shouldLoadContent = true;
                    p.contentLoading = true;
                    p.loadTimer = setTimeout(function () {
                        if (!p.shouldLoadContent)
                            return;

                        contents.load(url, function() {
                            p.contentLoaded = true;
                            p.contentLoading = false;
                            showPopup();
                        });
                    }, opts.loadDelay);
                }
            }
            // stops the hide event if we move from the trigger to the popup element
            clearTimeout(p.hideDelayTimer);
            // don't trigger the animation again if we're being shown
            if (!p.beingShown) {
                showPopup();
            }
        }).mouseout(function() {
            hidePopup();
        });

        contents.click(function(e) {
            e.stopPropagation();
        });

        $("body").click(function() {
            p.beingShown = false;
            clearTimeout(p.hideDelayTimer);
            clearTimeout(p.showTimer);
            popup.hide();
        });

        return popup;
    };

    $.contentHover.opts = {
        fadeTime: 100,
        hideDelay: 500,
        showDelay: 700,
        loadDelay: 50,
        mouseMoveThreshold: 10,
        container: "body"
    };

   
})(jQuery);
$(function(){
	 var users = [],
    contextPath = $('meta[name="thxcommon-baseUrl"]').attr('content');
	 var selectors = [
	                  "a.thxcommon-userlink",
	                  "a.userLogoLink"
	          ].join(", ");
	  $(selectors).each(function() {
         var userlink = $(this),
             userId = userlink.attr("data-userId");
         userlink.attr("title", "");
         var arrayIndex = $.inArray(userId, users);
         if (arrayIndex == -1) {
             users.push(userId);
             arrayIndex = $.inArray(userId, users);
         }
         $(this).addClass("userlink-" + arrayIndex);
     });
     $.each(users, function(i) {
         var url = contextPath + "/user/user!userLink.action?id=" + encodeURIComponent(users[i]);
         $.contentHover($(".userlink-" + i),i,url);
     });
});