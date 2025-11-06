using System.Threading.Tasks;
using CC_ConvUni_RESTDOTNET_GR01.Models;

namespace CC_ConvUni_RESTDOTNET_GR01.Services
{
    public interface IApiService
    {
        Task<ConversionResponse?> ConvertAsync(ConversionRequest request);
    }
}
