namespace CC_ConvUni_RESTDOTNET_GR01.Models
{
    public class ConversionRequest
    {
        public string Category { get; set; } = "";
        public string From { get; set; } = "";
        public string To { get; set; } = "";
        public double Value { get; set; }
    }
}
