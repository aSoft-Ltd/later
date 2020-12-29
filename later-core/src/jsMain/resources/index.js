const Later = require("./later-later-core.js").tz.co.asoft.Later;

const later = new Later((resolve,reject)=>{
    setTimeout(()=> resolve("43"),1000);
}).asPromise();

const pureLater = new Later((resolve,reject)=>{
    setTimeout(()=> resolve("2"),1000);
});

console.log("Running later");

console.log("pure promise ",new Promise((res,rej)=>{}));

console.log("mock promise ",later);

console.log("pure later " + pureLater)

const later2 = later.then(res=>{
    console.log(`Got ${res}`);
    return res*100
});

(async function() {
    const res2 = await later2;
    console.log("res2: "+res2);
})();