/*
*(c) Copyright 2011 Simone Masiero. Some Rights Reserved. 
*This work is licensed under a Creative Commons Attribution-Noncommercial-Share Alike 3.0 License
*/

$(
    function () {
        document.getElementById("console").addEventListener("touchstart", (event) => {
                        Typer.addText(event);
        })

    }
);

var rtn = new RegExp("\n", "g"); // newline regex
var rtt = new RegExp("\\t", "g"); // tab regex


var Typer = {
    text: null,
    accessCountimer: null,
    index: 0, // current cursor position
    speedMax: 12,
    speedMin: 1,
    writeCount: 0,
    speedStep: 0.5,
    speed: 10, // speed of the Typer
    file: "", //file, must be setted
    accessCount: 0, //times alt is pressed for Access Granted
    deniedCount: 0, //times caps is pressed for Access Denied
    secCount: 0, //times caps is pressed for Access Denied
    coldCount: 0, //times caps is pressed for Access Denied
    secCount: 0, //times caps is pressed for Access Denied
    radarCount: 0,
    windowCount: 0,
    ponyCount: 0,
    tagList: [],
    typeIntervalCounter: 0,
    typeInterval: false,
    init: function () {// inizialize Hacker Typer
        $.get(Typer.file, function (data) {// get the text file
            Typer.text = data;// save the textfile in Typer.text
        });
    },

    content: function () {
        return $("#console-code").html();// get console content
    },

    write: function (str) {// append to console content
        $("#console-code").append(str);
        return false;
    },


    removeText: function () {
        if (Typer.text) {

            Typer.index = (Typer.index > 0) ? Typer.index - Typer.speed * 2 : 0;
            Typer.addText(event);
        }
    },

    achievementGet: function () {
    },

    addTextAuto: function (key) {

        Typer.writeCount++;
        if (Typer.text) { // otherway if text is loaded

            if (Typer.index <= 0) {
                $("#console-code").html('');
            }

            Typer.index = Typer.index % Typer.text.length

            var cont = Typer.content(); // get the console content
            if (cont.substring(cont.length - 1, cont.length) == "_") // if the last char is the blinking cursor
                $("#console-code").html($("#console-code").html().substring(0, cont.length - 1)); // remove it before adding the text

            var text = Typer.text.substr(Typer.index, Typer.speed)//Typer.index-(Typer.speed));// parse the text for stripping html enities
            var rtn = new RegExp("\n", "g"); // newline regex
            //var rts= new RegExp("\\s", "g"); // whitespace regex
            var rtt = new RegExp("\\t", "g"); // tab regex
            text = text.replace(rtn, "<br/>").replace(rtt, "&nbsp;&nbsp;&nbsp;&nbsp;");//.replace(rts,"&nbsp;");// replace newline chars with br, tabs with 4 space and blanks with an html blank
            //console.log(text);

            $("#console-code").append(text);


            var usedTags = new RegExp("<img.*?>", "g"),
                systemTags = new RegExp("{(.*?)}", "g");
            var foundTag = usedTags.exec(Typer.text.substring(Typer.index)),
                foundSystemTag = systemTags.exec(Typer.text.substring(Typer.index));
            if (foundTag instanceof Array && foundTag.index <= Typer.speed) {
                Typer.index += foundTag.index + foundTag[0].length;
                $("#console-code").append(foundTag[0]);
            } else if (foundSystemTag instanceof Array && foundSystemTag.index <= Typer.speed) {
                //Typer.text.replace(/{(.*?)}/,foundSystemTag[1]);
                Typer.index += foundSystemTag.index + foundSystemTag[0].length;
                $("#console-code").append(foundSystemTag[1]);
            } else Typer.index += Typer.speed;
            $('body').scrollTop($("#console-code").height());
            window.scrollBy(0, 50); // scroll to make sure bottom is always visible

        }
        if (key.preventDefault && key.keyCode != 122) { // prevent F11(fullscreen) from being blocked
            key.preventDefault()
        }
        ;
        if (key.keyCode != 122) { // otherway prevent keys default behavior
            key.returnValue = false;
        }
    },

    addText: function (key) {//Main function to add the code
        Typer.writeCount++;
        const oConsoleCode = $("#console-code");

        if (Typer.text) { // otherway if text is loaded
            if (Typer.index <= 0) {
                oConsoleCode.html('');
            }

            Typer.index = Typer.index % Typer.text.length;

            var text = Typer.text.substr(Typer.index, Typer.speed);
            text = text.replace(rtn, "<br/>").replace(rtt, "&nbsp;&nbsp;&nbsp;&nbsp;");

            oConsoleCode.append(text);
            Typer.index += Typer.speed;

            $('body').scrollTop(oConsoleCode.height());
            window.scrollBy(0, 50); // scroll to make sure bottom is always visible

        }
    },

    updLstChr: function () { // blinking cursor
        var cont = this.content(); // get console
        if (cont.substring(cont.length - 1, cont.length) == "_") // if last char is the cursor
            $("#console-code").html($("#console-code").html().substring(0, cont.length - 1)); // remove it
        else
            this.write("_"); // else write it
    }
}

function startTime() {
    var today = new Date();
    var h = today.getHours();
    var m = today.getMinutes();
    var s = today.getSeconds();
// add a zero in front of numbers<10
    m = checkTime(m);
    s = checkTime(s);
    document.getElementById('txt').innerHTML = h + ":" + m + ":" + s;
    t = setTimeout(function () {
        startTime()
    }, 500);
}

function checkTime(i) {
    if (i < 10) {
        i = "0" + i;
    }
    return i;
}