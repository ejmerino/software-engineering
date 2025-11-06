namespace UnitConverter.Api.Models
{
    public class ConversionResult
    {
        public string Category { get; set; } = "";
        public string From { get; set; } = "";
        public string To { get; set; } = "";
        public double Input { get; set; }
        public double Result { get; set; }
    }
}
