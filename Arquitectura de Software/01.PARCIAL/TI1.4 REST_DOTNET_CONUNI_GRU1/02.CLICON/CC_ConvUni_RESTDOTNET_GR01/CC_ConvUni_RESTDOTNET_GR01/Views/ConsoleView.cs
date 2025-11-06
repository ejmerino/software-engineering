using CC_ConvUni_RESTDOTNET_GR01.Models;

namespace CC_ConvUni_RESTDOTNET_GR01.Views
{
    public static class ConsoleView
    {
        public static void ShowHeader()
        {
            Console.Clear();
            Console.WriteLine("=== Unit Converter (Consola) ===");
            Console.WriteLine("Servidor REST: http://localhost:5069/api/convert");
            Console.WriteLine();
        }

        public static void ShowMenu()
        {
            Console.WriteLine("Elige una opción:");
            Console.WriteLine("1) Realizar conversión");
            Console.WriteLine("2) Mostrar ejemplos de conversiones válidas");
            Console.WriteLine("0) Salir");
            Console.Write("> ");
        }

        public static ConversionRequest? ReadConversionInput()
        {
            Console.WriteLine();
            Console.Write("Categoría (mass | length | temperature): ");
            var category = Console.ReadLine()?.Trim();
            if (string.IsNullOrEmpty(category)) return null;

            Console.Write("Unidad desde (ej. kg, g, lb, oz, m, cm, km, mi, c, f, k, r): ");
            var from = Console.ReadLine()?.Trim();
            if (string.IsNullOrEmpty(from)) return null;

            Console.Write("Unidad a (to): ");
            var to = Console.ReadLine()?.Trim();
            if (string.IsNullOrEmpty(to)) return null;

            Console.Write("Valor (número): ");
            var vtxt = Console.ReadLine()?.Trim();
            if (!double.TryParse(vtxt, out var value))
            {
                Console.WriteLine("Valor inválido.");
                return null;
            }

            return new ConversionRequest
            {
                Category = category!,
                From = from!,
                To = to!,
                Value = value
            };
        }

        public static void ShowResult(ConversionResponse res)
        {
            Console.WriteLine();
            Console.WriteLine("=== Resultado ===");
            Console.WriteLine($"{res.Input} {res.From}  =>  {res.Result} {res.To}");
            Console.WriteLine();
        }

        public static void ShowError(string msg)
        {
            Console.WriteLine();
            Console.ForegroundColor = ConsoleColor.Red;
            Console.WriteLine("ERROR: " + msg);
            Console.ResetColor();
            Console.WriteLine();
        }

        public static void ShowExamples()
        {
            Console.WriteLine();
            Console.WriteLine("Ejemplos válidos:");
            Console.WriteLine("Masa: category=mass, units: kg, g, lb, oz");
            Console.WriteLine("Longitud: category=length, units: m, cm, km, mi");
            Console.WriteLine("Temperatura: category=temperature, units: c, f, k, r");
            Console.WriteLine("Ejemplo JSON body:");
            Console.WriteLine("{ \"category\":\"mass\",\"from\":\"kg\",\"to\":\"lb\",\"value\":2 }");
            Console.WriteLine();
        }

        public static (string user, string pass) ReadLogin()
        {
            Console.WriteLine();
            Console.Write("Usuario: ");
            var user = Console.ReadLine()?.Trim() ?? "";
            Console.Write("Password: ");
            // simple read password (no masking)
            var pass = Console.ReadLine() ?? "";
            return (user, pass);
        }

        public static void ShowLoginFailed()
        {
            Console.ForegroundColor = ConsoleColor.Yellow;
            Console.WriteLine("Usuario o contraseña incorrectos. Intenta nuevamente.");
            Console.ResetColor();
        }
    }
}
