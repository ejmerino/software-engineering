using System;
using System.Linq;
using System.Threading.Tasks;
using CC_ConvUni_RESTDOTNET_GR01.Models;
using CC_ConvUni_RESTDOTNET_GR01.Services;

namespace CC_ConvUni_RESTDOTNET_GR01.Controllers
{
    public class ConversionController
    {
        private readonly IApiService _api;
        private readonly IAuthService _auth;

        private readonly string[] validCategories = { "mass", "length", "temperature" };
        private readonly string[] massUnits = { "kg", "g", "lb", "oz" };
        private readonly string[] lengthUnits = { "m", "cm", "km", "ft" };
        private readonly string[] tempUnits = { "c", "f", "k", "r" };

        public ConversionController(IApiService api, IAuthService auth)
        {
            _api = api;
            _auth = auth;
        }

        private bool IsValidCategory(string category) => validCategories.Contains(category.ToLower());

        private string[] UnitsForCategory(string category) => category.ToLower() switch
        {
            "mass" => massUnits,
            "length" => lengthUnits,
            "temperature" => tempUnits,
            _ => Array.Empty<string>()
        };

        private bool IsValidUnit(string category, string unit) => UnitsForCategory(category).Contains(unit.ToLower());

        public async Task RunAsync()
        {
            while (true)
            {
                Console.WriteLine("---- Menú de Conversión ----");
                Console.WriteLine("Categorías disponibles: mass, length, temperature");

                string category;
                do
                {
                    Console.Write("Seleccione categoría o 'exit' para salir: ");
                    category = Console.ReadLine()?.Trim().ToLower() ?? "";
                    if (category == "exit") return;
                    if (!IsValidCategory(category))
                        Console.WriteLine("Categoría inválida. Intente de nuevo.");
                } while (!IsValidCategory(category));

                var availableUnits = UnitsForCategory(category);
                Console.WriteLine($"Unidades disponibles: {string.Join(", ", availableUnits)}");

                string from;
                do
                {
                    Console.Write("De (unidad): ");
                    from = Console.ReadLine()?.Trim().ToLower() ?? "";
                    if (!IsValidUnit(category, from))
                        Console.WriteLine("Unidad inválida. Intente de nuevo.");
                } while (!IsValidUnit(category, from));

                string to;
                do
                {
                    Console.Write("A (unidad): ");
                    to = Console.ReadLine()?.Trim().ToLower() ?? "";
                    if (!IsValidUnit(category, to))
                        Console.WriteLine("Unidad inválida. Intente de nuevo.");
                } while (!IsValidUnit(category, to));

                double value;
                while (true)
                {
                    Console.Write("Valor: ");
                    if (double.TryParse(Console.ReadLine(), out value)) break;
                    Console.WriteLine("Ingrese un número válido.");
                }

                var request = new ConversionRequest
                {
                    Category = category,
                    From = from,
                    To = to,
                    Value = value
                };

                var response = await _api.ConvertAsync(request);
                if (response != null)
                {
                    Console.WriteLine($"\nResultado: {value} {from} = {response.Result} {to}\n");
                }
                else
                {
                    Console.WriteLine("\nError al realizar la conversión.\n");
                }

                Console.WriteLine("Presione ENTER para continuar...");
                Console.ReadLine();
                Console.Clear();
            }
        }
    }
}
