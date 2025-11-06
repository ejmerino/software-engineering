using Microsoft.AspNetCore.Mvc;
using UnitConverter.Api.Models;
using UnitConverter.Api.Services;

namespace UnitConverter.Api.Controllers
{
    [ApiController]
    [Route("api/[controller]")]
    public class ConvertController : ControllerBase
    {
        private readonly IUnitConverter _converter;

        public ConvertController(IUnitConverter converter)
        {
            _converter = converter;
        }

        [HttpPost]
        public IActionResult Post([FromBody] ConversionRequest req)
        {
            if (req == null)
                return BadRequest(new { error = "Request body required" });

            if (string.IsNullOrWhiteSpace(req.Category) || string.IsNullOrWhiteSpace(req.From) || string.IsNullOrWhiteSpace(req.To))
                return BadRequest(new { error = "category, from and to are required" });

            var cat = req.Category.ToLower();
            if (!_converter.IsValidCategory(cat))
                return BadRequest(new { error = $"Unsupported category '{req.Category}'. Supported: mass,length,temperature" });

            try
            {
                var res = _converter.Convert(req);
                return Ok(res);
            }
            catch (ArgumentException ex)
            {
                return BadRequest(new { error = ex.Message });
            }
            catch (Exception ex)
            {
                // For dev: return error message. In prod, hide details.
                return StatusCode(500, new { error = "Internal server error", detail = ex.Message });
            }
        }

        [HttpGet("units/{category}")]
        public IActionResult Units(string category)
        {
            category = category?.ToLower() ?? "";
            if (!_converter.IsValidCategory(category))
                return BadRequest(new { error = "Unsupported category" });

            var list = _converter.SupportedUnits(category);
            return Ok(new { category, units = list });
        }
    }
}
