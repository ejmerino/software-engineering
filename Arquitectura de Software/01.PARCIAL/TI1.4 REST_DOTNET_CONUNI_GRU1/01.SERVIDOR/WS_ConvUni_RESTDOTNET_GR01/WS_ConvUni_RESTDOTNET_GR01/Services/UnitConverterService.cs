using System;
using System.Collections.Generic;
using UnitConverter.Api.Models;

namespace UnitConverter.Api.Services
{
    public class UnitConverterService : IUnitConverter
    {
        // Supported units per category
        private static readonly Dictionary<string, string[]> Units = new()
        {
            { "mass", new[] { "kg", "g", "lb", "oz" } },
            { "length", new[] { "m", "cm", "km", "mi" } },
            { "temperature", new[] { "c", "f", "k", "r" } } // r = Rankine
        };

        public bool IsValidCategory(string category) => Units.ContainsKey(category.ToLower());

        public IEnumerable<string> SupportedUnits(string category)
        {
            category = category.ToLower();
            if (!Units.ContainsKey(category)) return Array.Empty<string>();
            return Units[category];
        }

        public ConversionResult Convert(ConversionRequest req)
        {
            if (req == null) throw new ArgumentNullException(nameof(req));
            var category = req.Category.ToLower();
            var from = req.From.ToLower();
            var to = req.To.ToLower();
            var value = req.Value;

            if (!Units.ContainsKey(category))
                throw new ArgumentException($"Unsupported category '{req.Category}'. Supported: {string.Join(",", Units.Keys)}");

            if (Array.IndexOf(Units[category], from) < 0)
                throw new ArgumentException($"Unsupported unit '{req.From}' for category '{req.Category}'");

            if (Array.IndexOf(Units[category], to) < 0)
                throw new ArgumentException($"Unsupported unit '{req.To}' for category '{req.Category}'");

            double result = category switch
            {
                "mass" => ConvertMass(value, from, to),
                "length" => ConvertLength(value, from, to),
                "temperature" => ConvertTemperature(value, from, to),
                _ => throw new ArgumentException("Unsupported category")
            };

            return new ConversionResult
            {
                Category = category,
                From = from,
                To = to,
                Input = value,
                Result = result
            };
        }

        #region Mass conversions (base: kilogram)
        private static double ConvertMass(double value, string from, string to)
        {
            // convert from -> kg
            double inKg = from switch
            {
                "kg" => value,
                "g" => value / 1000.0,
                "lb" => value * 0.45359237,
                "oz" => value * 0.028349523125,
                _ => throw new ArgumentException("Unsupported mass unit")
            };

            // kg -> to
            return to switch
            {
                "kg" => inKg,
                "g" => inKg * 1000.0,
                "lb" => inKg / 0.45359237,
                "oz" => inKg / 0.028349523125,
                _ => throw new ArgumentException("Unsupported mass unit")
            };
        }
        #endregion

        #region Length conversions (base: meter)
        private static double ConvertLength(double value, string from, string to)
        {
            double inMeters = from switch
            {
                "m" => value,
                "cm" => value / 100.0,
                "km" => value * 1000.0,
                "mi" => value * 1609.344,
                _ => throw new ArgumentException("Unsupported length unit")
            };

            return to switch
            {
                "m" => inMeters,
                "cm" => inMeters * 100.0,
                "km" => inMeters / 1000.0,
                "mi" => inMeters / 1609.344,
                _ => throw new ArgumentException("Unsupported length unit")
            };
        }
        #endregion

        #region Temperature conversions (handle specially)
        // Celsius (c), Fahrenheit (f), Kelvin (k), Rankine (r)
        private static double ConvertTemperature(double value, string from, string to)
        {
            // Convert from -> Kelvin (k)
            double inK = from switch
            {
                "c" => value + 273.15,
                "f" => (value + 459.67) * (5.0 / 9.0),
                "k" => value,
                "r" => value * (5.0 / 9.0),
                _ => throw new ArgumentException("Unsupported temperature unit")
            };

            // Kelvin -> to
            return to switch
            {
                "c" => inK - 273.15,
                "f" => inK * (9.0 / 5.0) - 459.67,
                "k" => inK,
                "r" => inK * (9.0 / 5.0),
                _ => throw new ArgumentException("Unsupported temperature unit")
            };
        }
        #endregion
    }
}
