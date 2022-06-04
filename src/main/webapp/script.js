const wrapper = document.querySelector(".wrapper"),
//selecteaza primul element de clasa wrapper
inputPart = document.querySelector(".input-part"),
//selecteaza primul element de clasa input-part
infoTxt = inputPart.querySelector(".info-txt"),
//selecteaza primul element de clasa info-txt
inputField = inputPart.querySelector("input"),
//selecteaza primul element de tip input
weatherPart = wrapper.querySelector(".weather-part"),
//selecteaza primul element de clasa weather-part
wIcon = weatherPart.querySelector("img");
//selecteaza primul element de tip img

let api;
inputField.addEventListener("keyup", e =>{
    //daca am apasat Enter si valoarea introdusa nu e nula
    //atunci se face cererea api-ului apeland functia de mai jos
    if(e.key == "Enter" && inputField.value != ""){
        requestApi(inputField.value);
    }
});


function requestApi(city){
    api = `https://api.openweathermap.org/data/2.5/weather?q=${city}&units=metric&appid=27fdd072c17eda7fba0c51a911c9aaf5`;
    fetchData();
}


function fetchData(){
    infoTxt.innerText = "Wait...";
    //modifica textul elementului infoTxt in "Wait..." pentru a instiinta utilizatorul ca operatia de cautare este in desfasurare
    infoTxt.classList.add("pending");
    //adauga elementului infoTxt clasa "pending"
    fetch(api).then(res => res.json()).then(result => weatherDetails(result)).catch(() =>{
        infoTxt.innerText = "Something went wrong";
        infoTxt.classList.replace("pending", "error");
    });
}

function weatherDetails(info){
    console.log(info);
    if(info.cod == "404"){ // client side error: daca user-ul a cautat un oras care nu exista
        infoTxt.classList.replace("pending", "error");
        //elementului infoTxt i se modifica clasa din pending in error
        infoTxt.innerText = `${inputField.value} does not exist`;
        //in elementul infoTxt va fi afisat textul: "<valoarea introdusa de utilizator in campul inputField> does not exist"
    }
    else{
   
        const search_t = (info.dt+info.timezone)*1000;
        const local_t = new Date();
        const local_offset = (local_t.getTimezoneOffset())*60000;
        const current_t = new Date(search_t + local_offset);
        const sunrise_t = new Date((info.sys.sunrise+info.timezone)*1000+local_offset);
        const sunset_t = new Date((info.sys.sunset+info.timezone)*1000+local_offset);
        const city = info.name;
        const country = info.sys.country;
        const {description, id} = info.weather[0];
        const {temp, humidity} = info.main;
        const {speed, deg} = info.wind;
        const cloudy = info.clouds.all;
        //console.log(current_t.getTime());

        if(id == 800){
            if(current_t.getTime() > sunrise_t.getTime() && current_t.getTime() < sunset_t.getTime())
                wIcon.src = "Weather Icons/clear.png";
            else
                wIcon.src = "Weather Icons/moon.png";

        }
        else if(id >= 200 && id <= 232){
            wIcon.src = "Weather Icons/storm.png";  
        }
        else if(id >= 600 && id <= 622){
            wIcon.src = "Weather Icons/snow.png";
        }
        else if(id >= 701 && id <= 781){
            wIcon.src = "Weather Icons/fog.png";
        }
        else if(id == 801){
            if(current_t.getTime() > sunrise_t.getTime() && current_t.getTime() < sunset_t.getTime())
                wIcon.src = "Weather Icons/sun cloudy.png";
            else
                wIcon.src = "Weather Icons/moon cloudy.png"; 

        }
        else if(id >= 802 && id <= 804){
            wIcon.src = "Weather Icons/cloud.png";
        }
        else if((id >= 500 && id <= 531) || (id >= 300 && id <= 321)){
            wIcon.src = "Weather Icons/rain.png";
        }

        if (deg >= 337 || deg <= 22){
            weatherPart.querySelector(".speed-dir .dir").innerText = `N`
        }
        else if (deg > 22 && deg <= 67){
            weatherPart.querySelector(".speed-dir .dir").innerText = `NE`
        }
        else if (deg > 67 && deg <= 112){
            weatherPart.querySelector(".speed-dir .dir").innerText = `E`
        }
        else if (deg > 112 && deg <= 157){
            weatherPart.querySelector(".speed-dir .dir").innerText = `SE`
        }
        else if (deg > 157 && deg <= 202){
            weatherPart.querySelector(".speed-dir .dir").innerText = `S`
        }
        else if (deg > 202 && deg <= 247){
            weatherPart.querySelector(".speed-dir .dir").innerText = `SW`
        }
        else if (deg > 247 && deg <= 292){
            weatherPart.querySelector(".speed-dir .dir").innerText = `W`
        }
        else if (deg > 292 && deg < 337){
            weatherPart.querySelector(".speed-dir .dir").innerText = `NW`
        }
        
        weatherPart.querySelector(".temp .numb").innerText = Math.floor(temp);
        weatherPart.querySelector(".weather").innerText = description;
        weatherPart.querySelector(".location span").innerText = `${city}, ${country}`;
        weatherPart.querySelector(".speed-dir .speed").innerText = Math.floor(speed);
        weatherPart.querySelector(".humidity span").innerText = `${humidity}%`;
        weatherPart.querySelector(".clouds span").innerText = cloudy;
        infoTxt.classList.remove("pending", "error");
        //din lista de clase a elementului infoTxt sunt scoase clasele pending si error
        infoTxt.innerText = "";
        //textul din elementul infoTxt este sters
        inputField.value = "";
        //textul introdus de utilizator in campul inputField este sters
        wrapper.classList.add("active");
        //elementului wrapper ii este adaugat in lista de clase clasa active
    }
}

