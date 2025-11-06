namespace CC_ConvUni_RESTDOTNET_GR01.Models
{
    public class ConversionResponse
    {
        public string Category { get; set; } = "";
        public string From { get; set; } = "";
        public string To { get; set; } = "";
        public double Input { get; set; }
        public double Result { get; set; }
    }
}
