namespace EurekaSucursalesApi.Models
{
    public class Sucursal
    {
        public int Id { get; set; }

        public string Nombre { get; set; } = string.Empty;

        public string Ciudad { get; set; } = string.Empty;

        public string Direccion { get; set; } = string.Empty;

        // Coincide con FLOAT / REAL en SQL Server
        public double Latitud { get; set; }

        public double Longitud { get; set; }
    }
}
