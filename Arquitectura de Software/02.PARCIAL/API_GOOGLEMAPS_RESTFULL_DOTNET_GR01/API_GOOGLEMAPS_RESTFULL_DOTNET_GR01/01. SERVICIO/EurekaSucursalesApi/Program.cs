using EurekaSucursalesApi.Data;
using Microsoft.EntityFrameworkCore;

var builder = WebApplication.CreateBuilder(args);

builder.Services.AddControllers();

// EF Core SQL Server
builder.Services.AddDbContext<AppDbContext>(options =>
    options.UseSqlServer(builder.Configuration.GetConnectionString("DefaultConnection"))
);

// ✅ CORS (para React en localhost y por IP)
builder.Services.AddCors(options =>
{
    options.AddPolicy("AllowReact", policy =>
    {
        policy
            .AllowAnyHeader()
            .AllowAnyMethod()
            .AllowCredentials()
            .SetIsOriginAllowed(origin =>
            {
                // Permite localhost y cualquier IP en tu red (5173 típico de Vite)
                return origin.StartsWith("http://localhost:5173")
                    || origin.StartsWith("http://127.0.0.1:5173")
                    || origin.StartsWith("http://192.168.");
            });
    });
});

// Swagger
builder.Services.AddEndpointsApiExplorer();
builder.Services.AddSwaggerGen();

var app = builder.Build();

app.UseSwagger();
app.UseSwaggerUI();

// ✅ Usa CORS antes de MapControllers
app.UseCors("AllowReact");

app.MapControllers();

app.Run();