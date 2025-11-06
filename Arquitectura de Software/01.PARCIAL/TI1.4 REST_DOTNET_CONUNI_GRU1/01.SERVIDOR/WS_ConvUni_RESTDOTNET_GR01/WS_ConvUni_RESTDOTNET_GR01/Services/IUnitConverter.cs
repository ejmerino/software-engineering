using UnitConverter.Api.Models;

namespace UnitConverter.Api.Services
{
    public interface IUnitConverter
    {
        ConversionResult Convert(ConversionRequest req);
        bool IsValidCategory(string category);
        IEnumerable<string> SupportedUnits(string category);
    }
}
