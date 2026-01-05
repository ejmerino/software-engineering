using EurekaSucursalesApi.Data;
using EurekaSucursalesApi.Models;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;

namespace EurekaSucursalesApi.Controllers
{
    [ApiController]
    [Route("api/[controller]")]
    public class SucursalesController : ControllerBase
    {
        private readonly AppDbContext _db;

        public SucursalesController(AppDbContext db)
        {
            _db = db;
        }

        // GET: api/sucursales
        [HttpGet]
        public async Task<ActionResult<IEnumerable<Sucursal>>> GetAll()
        {
            var items = await _db.Sucursales
                .AsNoTracking()
                .OrderBy(x => x.Id)
                .ToListAsync();

            return Ok(items);
        }

        // GET: api/sucursales/5
        [HttpGet("{id:int}")]
        public async Task<ActionResult<Sucursal>> GetById(int id)
        {
            var item = await _db.Sucursales
                .AsNoTracking()
                .FirstOrDefaultAsync(x => x.Id == id);

            if (item == null) return NotFound(new { message = "Sucursal no encontrada." });

            return Ok(item);
        }

        // POST: api/sucursales
        [HttpPost]
        public async Task<ActionResult<Sucursal>> Create([FromBody] Sucursal sucursal)
        {
            if (string.IsNullOrWhiteSpace(sucursal.Nombre))
                return BadRequest(new { message = "Nombre es obligatorio." });

            _db.Sucursales.Add(sucursal);
            await _db.SaveChangesAsync();

            return CreatedAtAction(nameof(GetById), new { id = sucursal.Id }, sucursal);
        }

        // PUT: api/sucursales/5
        [HttpPut("{id:int}")]
        public async Task<IActionResult> Update(int id, [FromBody] Sucursal sucursal)
        {
            if (id != sucursal.Id)
                return BadRequest(new { message = "El id de la URL no coincide con el id del body." });

            var exists = await _db.Sucursales.AnyAsync(x => x.Id == id);
            if (!exists) return NotFound(new { message = "Sucursal no encontrada." });

            _db.Entry(sucursal).State = EntityState.Modified;
            await _db.SaveChangesAsync();

            return NoContent();
        }

        // DELETE: api/sucursales/5
        [HttpDelete("{id:int}")]
        public async Task<IActionResult> Delete(int id)
        {
            var item = await _db.Sucursales.FirstOrDefaultAsync(x => x.Id == id);
            if (item == null) return NotFound(new { message = "Sucursal no encontrada." });

            _db.Sucursales.Remove(item);
            await _db.SaveChangesAsync();

            return NoContent();
        }
    }
}
