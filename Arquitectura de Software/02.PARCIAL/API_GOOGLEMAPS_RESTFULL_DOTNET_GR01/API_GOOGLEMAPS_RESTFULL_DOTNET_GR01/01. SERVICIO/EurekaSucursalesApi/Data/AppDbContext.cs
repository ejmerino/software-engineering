using Microsoft.EntityFrameworkCore;
using EurekaSucursalesApi.Models;

namespace EurekaSucursalesApi.Data
{
    public class AppDbContext : DbContext
    {
        public AppDbContext(DbContextOptions<AppDbContext> options)
            : base(options)
        {
        }

        public DbSet<Sucursal> Sucursales => Set<Sucursal>();

        protected override void OnModelCreating(ModelBuilder modelBuilder)
        {
            modelBuilder.Entity<Sucursal>(entity =>
            {
                entity.ToTable("Sucursal", "dbo");

                entity.HasKey(e => e.Id);

                entity.Property(e => e.Nombre)
                      .IsRequired()
                      .HasMaxLength(200);

                entity.Property(e => e.Ciudad)
                      .IsRequired()
                      .HasMaxLength(100);

                entity.Property(e => e.Direccion)
                      .IsRequired()
                      .HasMaxLength(250);

                // Coincide con FLOAT / REAL
                entity.Property(e => e.Latitud)
                      .HasColumnType("float");

                entity.Property(e => e.Longitud)
                      .HasColumnType("float");
            });
        }
    }
}
