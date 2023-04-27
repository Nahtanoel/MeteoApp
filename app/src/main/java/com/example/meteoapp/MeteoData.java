package com.example.meteoapp;

public class MeteoData {

    private String temperature;
    private String windspeed;
    private String winddirection;
    private String weathercode;
    protected String Color_bg;


    public String getTemperature() {
        return temperature + "°C";
    }

    public String getWeathercode() {
        return weathercode;
    }

    public String getWinddirection() {
        return "Direction du vent : "+winddirection;
    }

    public int getWindDirectionDegree(){
        return Integer.parseInt(winddirection);
    }

    public String getWindspeed() {
        return "WindSpeed : "+windspeed+" km/h";
    }

    public MeteoData(String temperature,String windspeed,String winddirection,String weathercode){
        this.temperature=temperature;
        this.windspeed=windspeed;
        this.winddirection=winddirection;

        int dec = Integer.parseInt(weathercode);
        if(dec == 0){
            this.weathercode="Ciel dégagé";
            Color_bg="#46b7e8";

        }else if (dec == 1){
            this.weathercode="Principalement dégagé";
            Color_bg="#5ea5c4";

        }else if (dec == 2){
            this.weathercode="Partiellement nuageux ";
            Color_bg="#8db9cc";

        }else if (dec == 3){
            this.weathercode="Couvert";
            Color_bg="#a2b3ba";

        }else if (dec == 45){
            this.weathercode="Brouillard";
            Color_bg="#c5cfd4";
        }else if (dec == 48){
            this.weathercode="Brouillard de givre déposant";
            Color_bg="#c5cfd4";

        }else if (dec == 51){
            this.weathercode="Bruine  d'intensité légère";
            Color_bg="#c5cfd4";

        }else if (dec == 53){
            this.weathercode="Bruine  d'intensité modérée";
            Color_bg="#c5cfd4";

        }else if (dec == 55){
            this.weathercode="Bruine  d'intensité dense";
            Color_bg="#c5cfd4";

        }else if (dec == 56){
            this.weathercode="Bruine verglaçante d'intensité légère";
            Color_bg="#c5cfd4";

        }else if (dec == 57){
            this.weathercode="Bruine verglaçante d'intensité dense";
            Color_bg="#c5cfd4";
        }else if (dec == 61){
            this.weathercode="Pluie intensité légère";
            Color_bg="#b2d9ed";

        }else if (dec == 63){
            this.weathercode="Pluie intensité modérée";
            Color_bg="#b2d9ed";

        }else if (dec == 65){
            this.weathercode="Pluie intensité forte";
            Color_bg="#76badb";

        }else if (dec == 66){
            this.weathercode="Pluie verglaçante intensité légère";
            Color_bg="#76badb";

        }else if (dec == 67){
            this.weathercode="Pluie verglaçante intensité forte";
            Color_bg="#76badb";

        }else if (dec == 71){
            this.weathercode="Chute de neige légère";
            Color_bg="#dae4e8";

        }else if (dec == 73){
            this.weathercode="Chute de neige modérée";
            Color_bg="#dae4e8";


        }else if (dec == 75){
            this.weathercode="Chute de neige forte";
            Color_bg="#dae4e8";

        }else if (dec == 77){
            this.weathercode="Granules de neige";
            Color_bg="#dae4e8";

        }else if (dec == 80){
            this.weathercode="Averses de pluie légère";
            Color_bg="#b2d9ed";

        }else if (dec == 81){
            this.weathercode="Averses de pluie modérée";
            Color_bg="#b2d9ed";

        }else if (dec == 82){
            this.weathercode="Averses de pluie violente";
            Color_bg="#b2d9ed";
        }else if (dec == 85){
            this.weathercode="Averses de neige légère";
            Color_bg="#dae4e8";
        }else if (dec == 86){
            this.weathercode="Averses de neige forte";
            Color_bg="#dae4e8";
        }else if (dec == 95){
            this.weathercode="Orage : intensité légère ou modérée";
            Color_bg="#757778";
        }else if (dec == 96){
            this.weathercode="Orage avec grêle légère";
            Color_bg="#757778";
        }else{
            this.weathercode="Orage avec grêle forte";
            Color_bg="#757778";
        }



    }

    public String toString(){
        return  "Temp : "           + this.temperature  +
                " WindSpeed : "     + this.windspeed    +
                " WindDir : "       + this.winddirection+
                " WeatherCode : "   + this.weathercode  +
                "Color of BG  : "   + this.Color_bg;
    }
}
