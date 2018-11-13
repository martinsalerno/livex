var counterGlobal = 0;
var World = {

    init: function initFn() {
        this.createOverlays();
    },

    createOverlays: function createOverlaysFn() {
        this.targetCollectionResource = new AR.TargetCollectionResource("assets/livex.wtc", {
            onError: World.onError
        });

        this.tracker = new AR.ImageTracker(this.targetCollectionResource, {
            onTargetsLoaded: World.showInfoBar,
            onError: World.onError
        });

        this.imgButton = new AR.ImageResource("assets/tickets.jpg");
         var buyTicketsButton = this.createButton("http://www.livepass.com.ar", 0.2, {
             translate: {
                x: 0,
                y: 0.4
             },
             zOrder: 1
          });

        var video = new AR.VideoDrawable("assets/livex_rhcp_example.mp4", 0.7, {
            translate: {
                x: 0,
                y: 0
            },
             onClick: function videoClicked () {
                if (video.playing) {
                    video.pause();
                    video.playing = false;
                    playButton.enabled = true;
                } else {
                    video.resume();
                    video.playing = true;
                    playButton.enabled = true;
                }
            },
            enabled: true
        });

        var video2 = new AR.VideoDrawable("assets/livex_babasonicos_example.mp4", 0.7, {
            translate: {
                x: 0,
                y: 0
            },
            onClick: function videoClicked () {
                if (video2.playing) {
                    video2.pause();
                    video2.playing = false;
                    playButton.enabled = true;
                } else {
                    video2.resume();
                    video2.playing = true;
                    playButton.enabled = true;
                }
            },
             enabled: false
        });

        var video3 = new AR.VideoDrawable("assets/livex_arctic_monkeys_example.mp4", 0.7, {
            translate: {
                x: 0,
                y: 0
            },
            onClick: function videoClicked () {
                if (video3.playing) {
                    video3.pause();
                    video3.playing = false;
                    playButton.enabled = true;
                } else {
                    video3.resume();
                    video3.playing = true;
                    playButton.enabled = true;
                }
            },
            enabled: false
        });

        this.videos = [{video: video, message: "RED HOT CHILI PEPPERS"}, {video: video2, message: "BABASONICOS"}, {video: video3, message: "ARCTIC MONKEYS"}];

        var slideshow = new AR.HtmlDrawable({
            uri: "assets/slideshow.html"
        }, 1, {
            translate: {
                x: 0,
                y: -0.8
            },
            onError: World.onError
        });

       this.imgButton2 = new AR.ImageResource("assets/button_artists.jpg");

       var counter = 0;

       var buyTicketsButton2 = new AR.ImageDrawable(this.imgButton2, 0.2, {
          translate: {
             x: 0,
             y: -0.4
          },
          zOrder: 1,
          onClick: function changeVideoPlaying() {
             World.changeMessage("counter local " + counter);
             counter = counter + 1;
             if (counter == 3){
               counter = 0;
             }
             counterGlobal = counter;
             World.changeVideo();
          }
        });

        this.pageOne = new AR.ImageTrackable(this.tracker, "livex_senior_anillos", {
            drawables: {
                cam: [buyTicketsButton, video, slideshow],
            },
                onImageRecognized: function onImageRecognizedFn() {
                    if (this.hasVideoStarted) {
                        video.resume();
                    }
                    else {
                        this.hasVideoStarted = true;
                        video.play(-1);
                    }
                    World.changeMessage("RED HOT CHILI PEPPERS - PERSONAL FEST");
                },
                onImageLost: function onImageLostFn() {
                    video.pause();
                    World.changeMessage("Detectando..");

                },
            onError: World.onError
        });

        this.pageTwo = new AR.ImageTrackable(this.tracker, "entrada.*", {
            drawables: {
                cam: [buyTicketsButton2, this.videos[counter].video]
            },
                onImageRecognized: function onImageRecognizedFn() {
                    if (this.hasVideoStarted) {
                        this.drawables.cam[1].resume();
                        var markerSelectedJSON = {
                           name: "entradaScanned",
                           id: "5b9ae620c399fb6a8e4eaade"
                        };
                        AR.platform.sendJSONObject(markerSelectedJSON);
                    }
                    else {
                        this.hasVideoStarted = true;
                        this.drawables.cam[1].play(-1);
                    }
                    World.changeMessage("PERSONAL FEST - Livex");
                },
                onImageLost: function onImageLostFn() {
                    this.drawables.cam[1].pause();
                    World.changeMessage("Detectando..");

                },
            onError: World.onError
        });
    },

    onError: function onErrorFn(error) {
        alert(error);
    },


    createButton: function createButtonFn(url, size, options) {

        options.onClick = function() {
            AR.context.openInBrowser(url);
        };
        return new AR.ImageDrawable(this.imgButton, size, options);
    },

    createButton2: function createButtonFn2(url, size, options) {
        options.onClick = function() {
            World.pageTwo.changeVideo();
        };

        return new AR.ImageDrawable(this.imgButton2, size, options);
    },

    changeMessage: function worldLoadedFn(text) {
        document.getElementById("loadingMessage").innerHTML = text;
    },

    changeVideo: function changeVideo(indexNew, indexOld) {
       this.pageTwo.drawables.cam[1].pause();
       this.pageTwo.drawables.cam[1].enabled = false;
       this.pageTwo.drawables.removeCamDrawable(1);
       this.pageTwo.drawables.addCamDrawable(this.videos[counterGlobal].video, 1);
       this.changeMessage("PERSONAL FEST - " + this.videos[counterGlobal].message);
       this.pageTwo.drawables.cam[1].enabled = true;
       this.pageTwo.drawables.cam[1].resume();

    },

    hideInfoBar: function hideInfoBarFn() {
        document.getElementById("infoBox").style.display = "none";
    }

};
World.init();