namespace UnitConverter.Api.Models
{
    public class ConversionRequest
    {
        public string Category { get; set; } = ""; // "mass" | "length" | "temperature"
        public string From { get; set; } = "";
        public string To { get; set; } = "";
        public double Value { get; set; }
    }
}
