/**
 * Created by tia on 15/1/10.
 */
function ajaxRequest(url){
    $.ajax({
        type : "get",
        url : "url",
        async : false,
        success : function(data){
            console.log("得到数据："+data)
            alert(data)
            return data
        }
    });
    return false;
}
var pointsss = [[121.481189,31.213423],[121.535199,31.633306],[121.404977,31.217903],[121.479563,31.236565],[113.488573,23.187666],[121.441348,31.196092],[121.431,31.226729],[121.454808,31.235487],[121.404451,31.255881],[121.458,31.288216],[121.510604,31.270398],[121.535841,31.304743],[121.388517,31.119509],[121.494991,31.410212],[121.273148,31.379384],[121.551956,31.228178],[121.2485,30.835149],[121.233402,31.038821],[121.129657,31.154846],[121.482166,30.923301],[119.368487,33.013803],[118.801358,32.065315],[120.318392,31.497894],[117.290729,34.210674],[119.981574,31.815347],[120.581388,31.308608],[120.89766,31.986983],[119.225902,34.604639],[119.02185,33.615894],[120.164682,33.354052],[119.418148,32.399604],[119.43212,32.195976],[119.93211,32.461309],[118.284101,33.96774],[119.957117,29.159687],[120.165908,30.279206],[121.557734,29.878745],[120.701558,28.003348],[120.761865,30.751401],[120.094699,30.898255],[120.586718,30.037139],[119.657175,29.086234],[118.879866,28.943762],[122.215865,29.990477],[121.42739,28.662959],[119.930726,28.472813],[117.216054,31.85929],[117.233256,31.82719],[118.435275,31.359606],[117.006566,32.632353],[118.514445,31.677466],[118.322845,32.308074]]
var indi = ['社会经济','人居环境','自然要素','生活社会','新兴数据'];
var map = ['GDP','常驻人口总数','绿化率','PM2.5','污水处理厂'];
//var region2;
var by = function(name,minor)
{
    return function(o, p)
    {
        var a, b;
        if (typeof o === "object" && typeof p === "object" && o && p)
        {
            a = o[name];
            b = p[name];
            if (a === b) {return typeof minor==='function' ?minor(o,p):0;}
            if (typeof a === typeof b) { return a < b ? -1 : 1;}
            return typeof a < typeof b ? -1 : 1;
        }
        else {throw ("error"); }
    }
}
