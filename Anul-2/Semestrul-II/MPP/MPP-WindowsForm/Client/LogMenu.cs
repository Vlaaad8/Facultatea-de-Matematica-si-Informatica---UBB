using MPP_CSharpProject.domain;
using Services;

namespace MPP_WindowsForm;

public partial class LogMenu : Form
{
    private readonly ClientController _clientController;
    private readonly IServices _service;

    public LogMenu(ClientController clientController, IServices service)
    {
        InitializeComponent();
        _clientController = clientController;
        _service = service;
    }

    private void loginButton_Click(object sender, EventArgs e)
    {
        var userText = userBox.Text;
        var passwordText = passwordBox.Text;

        userBox.Clear();
        passwordBox.Clear();
        
        Employee employee = _clientController.Login(userText, passwordText);

        if (employee != null)
        {
            MainMenu mainWindow = new MainMenu(_service, _clientController);
            mainWindow.Show();
            this.Hide();
        }
        else
        {
            errorText.Text = "Login failed.";
            errorText.ForeColor = Color.Red;
        }
    }
}
