using System;
using System.Collections.Generic;
using System.Linq;
using System.Runtime.Serialization;
using System.ServiceModel;
using System.Text;

namespace WS_ConvUni_SOAPDOTNET_GR01
{
    // NOTA: puede usar el comando "Rename" del menú "Refactorizar" para cambiar el nombre de interfaz "IAlumnosServices" en el código y en el archivo de configuración a la vez.
    [ServiceContract]
    public interface IAlumnosServices
    {
        [OperationContract]
        void DoWork();
    }
}
