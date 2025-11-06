namespace CC_ConvUni_RESTDOTNET_GR01.Services
{
    public class AuthService : IAuthService
    {
        private const string USER = "MONSTER";
        private const string PASS = "MONSTER9";

        public bool Login(string username, string password)
        {
            return username == USER && password == PASS;
        }
    }
}
