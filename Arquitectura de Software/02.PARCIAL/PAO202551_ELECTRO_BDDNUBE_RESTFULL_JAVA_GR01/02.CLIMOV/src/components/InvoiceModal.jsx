import { useEffect } from 'react';

export default function InvoiceModal({ isOpen, onClose, factura, amortizacion }) {
  if (!isOpen || !factura) return null;

  // Formateador de moneda
  const money = (val) => new Intl.NumberFormat('en-US', { style: 'currency', currency: 'USD' }).format(val);

  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center p-4 bg-slate-900/80 backdrop-blur-sm animate-fadeIn">
      <div className="bg-white rounded-2xl shadow-2xl w-full max-w-2xl max-h-[90vh] overflow-y-auto flex flex-col">
        
        {/* Cabecera Modal */}
        <div className="bg-gradient-to-r from-blue-600 to-slate-800 p-6 text-white flex justify-between items-center sticky top-0 z-10">
          <div>
            <h2 className="text-2xl font-bold">¡Venta Exitosa!</h2>
            <p className="text-blue-200 text-sm">Comprobante #{factura.idFactura}</p>
          </div>
          <button onClick={onClose} className="text-white/70 hover:text-white text-2xl font-bold">&times;</button>
        </div>

        {/* Contenido del Ticket */}
        <div className="p-8 space-y-6">
          {/* Datos Cliente */}
          <div className="grid grid-cols-2 gap-4 border-b border-slate-100 pb-4">
            <div>
              <p className="text-xs text-slate-400 uppercase font-bold">Cliente</p>
              <p className="font-semibold text-slate-800">{factura.cliente?.nombres} {factura.cliente?.apellidos}</p>
              <p className="text-sm text-slate-500">{factura.cliente?.cedula}</p>
            </div>
            <div className="text-right">
              <p className="text-xs text-slate-400 uppercase font-bold">Fecha</p>
              <p className="font-semibold text-slate-800">{new Date(factura.fecha).toLocaleDateString()}</p>
              <p className="text-sm text-slate-500">{factura.formaPago}</p>
            </div>
          </div>

          {/* Lista de Productos */}
          <div>
             <table className="w-full text-sm">
                <thead className="text-slate-400 border-b border-slate-100">
                    <tr>
                        <th className="text-left py-2">Producto</th>
                        <th className="text-center py-2">Cant.</th>
                        <th className="text-right py-2">Subtotal</th>
                    </tr>
                </thead>
                <tbody className="divide-y divide-slate-50">
                    {factura.detalles?.map((d, i) => (
                        <tr key={i}>
                            <td className="py-3 text-slate-700 font-medium">
                                {d.electrodomestico?.nombre || 'Item'}
                            </td>
                            <td className="py-3 text-center text-slate-500">{d.cantidad}</td>
                            <td className="py-3 text-right text-slate-700">{money(d.subtotalLinea)}</td>
                        </tr>
                    ))}
                </tbody>
             </table>
          </div>

          {/* Totales */}
          <div className="bg-slate-50 p-4 rounded-xl space-y-2">
             <div className="flex justify-between text-slate-500">
                <span>Subtotal</span>
                <span>{money(factura.subtotal)}</span>
             </div>
             <div className="flex justify-between text-green-600">
                <span>Descuento</span>
                <span>- {money(factura.descuento)}</span>
             </div>
             <div className="flex justify-between text-xl font-bold text-slate-800 border-t border-slate-200 pt-2 mt-2">
                <span>TOTAL</span>
                <span>{money(factura.total)}</span>
             </div>
          </div>

          {/* Tabla de Amortización (Si existe) */}
          {amortizacion && amortizacion.length > 0 && (
            <div className="mt-6">
                <h3 className="text-lg font-bold text-slate-800 mb-2 border-l-4 border-purple-500 pl-2">Plan de Pagos (Crédito)</h3>
                <div className="overflow-x-auto border rounded-lg">
                    <table className="w-full text-xs text-left">
                        <thead className="bg-purple-50 text-purple-900 font-bold">
                            <tr>
                                <th className="p-2">#</th>
                                <th className="p-2">Fecha</th>
                                <th className="p-2">Cuota</th>
                                <th className="p-2">Saldo</th>
                            </tr>
                        </thead>
                        <tbody>
                            {amortizacion.map((a, idx) => (
                                <tr key={idx} className="border-t hover:bg-slate-50">
                                    <td className="p-2">{a.numeroCuota}</td>
                                    <td className="p-2">{new Date(a.fechaPagoProgramada).toLocaleDateString()}</td>
                                    <td className="p-2 font-bold">{money(a.valorCuota)}</td>
                                    <td className="p-2 text-slate-500">{money(a.saldoCapital)}</td>
                                </tr>
                            ))}
                        </tbody>
                    </table>
                </div>
            </div>
          )}
        </div>

        {/* Pie de Modal */}
        <div className="bg-slate-50 p-4 flex justify-end gap-3 sticky bottom-0">
            <button onClick={() => window.print()} className="px-4 py-2 text-slate-600 font-bold hover:bg-slate-200 rounded-lg transition">
                🖨️ Imprimir
            </button>
            <button onClick={onClose} className="px-6 py-2 bg-blue-600 text-white font-bold rounded-lg hover:bg-blue-700 shadow-lg shadow-blue-500/30 transition">
                Aceptar y Cerrar
            </button>
        </div>
      </div>
    </div>
  );
}