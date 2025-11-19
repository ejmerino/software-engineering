package ec.edu.monster.controller;
// ... (Pega el mismo código que te di en el mensaje anterior)
import ec.edu.monster.service.ComercializadoraService;
import ec.edu.monster.view.LoginView;
import ec.edu.monster.view.MainAppView;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.SwingUtilities;

public class LoginController {
    
    private LoginView view;

    public LoginController(LoginView view) {
        this.view = view;
        iniciarEventos();
    }
    
    private void iniciarEventos() {
        view.getBtnIngresar().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Thread(() -> {
                    iniciarLogin();
                }).start();
            }
        });
    }

    private void iniciarLogin() {
        String user = view.getTxtUsuario().getText();
        String pass = new String(view.getTxtPassword().getPassword());
        
        SwingUtilities.invokeLater(() -> {
            if ("MONSTER".equals(user) && "MONSTER9".equals(pass)) {
                view.getLblError().setText("¡Bienvenido!");
                view.getLblError().setForeground(Color.GREEN);
                
                MainAppView mainView = new MainAppView();
                ComercializadoraService service = new ComercializadoraService();
                new MainAppController(mainView, service); // Llama al otro controlador
                
                mainView.setLocationRelativeTo(null); // Centra la ventana principal
                mainView.setVisible(true);
                view.dispose(); 

            } else {
                view.getLblError().setText("Error: Usuario o contraseña incorrectos.");
                view.getLblError().setForeground(Color.RED);
            }
        });
    }
}