using System.Net.Http;
using System.Net.Http.Json;
using System.Threading.Tasks;
using CC_ConvUni_RESTDOTNET_GR01.Models;

namespace CC_ConvUni_RESTDOTNET_GR01.Services
{
    public class ApiService : IApiService
    {
        private readonly HttpClient _client;

        public ApiService(HttpClient client)
        {
            _client = client;
        }

        public async Task<ConversionResponse?> ConvertAsync(ConversionRequest request)
        {
            var response = await _client.PostAsJsonAsync("api/Convert", request);
            if (response.IsSuccessStatusCode)
            {
                return await response.Content.ReadFromJsonAsync<ConversionResponse>();
            }
            return null;
        }
    }
}
