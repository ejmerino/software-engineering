using System;
using System.Net.Http;
using System.Threading.Tasks;
using CC_ConvUni_RESTDOTNET_GR01.Services;
using CC_ConvUni_RESTDOTNET_GR01.Controllers;

namespace CC_ConvUni_RESTDOTNET_GR01
{
    internal class Program
    {
        static async Task Main(string[] args)
        {
            var authService = new AuthService();
            bool logged = false;

            Console.WriteLine("=== Bienvenido al Cliente de Conversión de Unidades ===\n");

            while (!logged)
            {
                Console.Write("Usuario: ");
                string user = Console.ReadLine()!;

                Console.Write("Contraseña: ");
                string pass = "";
                ConsoleKeyInfo key;
                do
                {
                    key = Console.ReadKey(true);
                    if (key.Key != ConsoleKey.Backspace && key.Key != ConsoleKey.Enter)
                    {
                        pass += key.KeyChar;
                        Console.Write("*");
                    }
                    else if (key.Key == ConsoleKey.Backspace && pass.Length > 0)
                    {
                        pass = pass[0..^1];
                        Console.Write("\b \b");
                    }
                } while (key.Key != ConsoleKey.Enter);
                Console.WriteLine();

                logged = authService.Login(user, pass);
                if (!logged)
                    Console.WriteLine("Credenciales incorrectas. Intenta de nuevo.\n");
            }

            Console.WriteLine("\n¡Login exitoso!\n");

            using var httpClient = new HttpClient { BaseAddress = new Uri("http://localhost:5069/") };
            var apiService = new ApiService(httpClient);
            var controller = new ConversionController(apiService, authService);

            await controller.RunAsync();
        }
    }
}
