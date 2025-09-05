using System.Data;
using DefaultNamespace;
using log4net;
using MPP_CSharpProject.domain;
using Services;

namespace MPP_WindowsForm;

public partial class MainMenu : Form
{
    private readonly ClientController _clientController;
    private readonly IServices _service;
    private BindingSource bindingSource;
    private BuyMenu buyMenu;
    private static readonly ILog log = LogManager.GetLogger(typeof(MainMenu));

    public MainMenu(IServices service, ClientController clientController)
    {
        InitializeComponent();
        _service = service;
        _clientController = clientController;

        SetupComponents();
        LoadData();

        _clientController.updateEvent += FlightsUpdate;
    }

    private void SetupComponents()
    {
        mainTable.AutoGenerateColumns = false;

        mainTable.Columns.AddRange(new DataGridViewColumn[]
        {
            new DataGridViewTextBoxColumn { HeaderText = "Origin", DataPropertyName = "origin", Name = "originColumn" },
            new DataGridViewTextBoxColumn { HeaderText = "Departure", DataPropertyName = "departure", Name = "departureColumn" },
            new DataGridViewTextBoxColumn { HeaderText = "Airport", DataPropertyName = "airport", Name = "airportColumn" },
            new DataGridViewTextBoxColumn { HeaderText = "Date", DataPropertyName = "dayTime", Name = "dayTimeColumn" },
            new DataGridViewTextBoxColumn { HeaderText = "Available Seats", DataPropertyName = "availableSeats", Name = "seatsColumn" }
        });
    }

    private void LoadData()
    {
        bindingSource = new BindingSource
        {
            DataSource = _service.GetAllFlights().ToList()
        };
        mainTable.DataSource = bindingSource;

        originBox.DataSource = _service.GetOrigin().ToList();
        departureBox.DataSource = _service.GetDestination().ToList();
    }

    private void searchButton1_Click(object sender, EventArgs e)
    {
        string origin = originBox.Text;
        string departure = departureBox.Text;
        DateTime date = dateBox.Value.Date;

        buyMenu = new BuyMenu(_service, origin, departure, date);
        buyMenu.Show();
    }

    private void logOutButton_Click(object sender, EventArgs e)
    {
        this.Close();

        if (buyMenu != null && !buyMenu.IsDisposed)
        {
            buyMenu.Close();
        }
    }

    private void UpdateDataGridView(DataGridView table, IList<Flight> newData)
    {
        log.Info("UpdateDataGridView Called!");
        bindingSource = new BindingSource { DataSource = newData };
        table.DataSource = null;
        table.DataSource = bindingSource;
    }

    public delegate void UpdateGridCallback(DataGridView table, IList<Flight> newData);

    public void FlightsUpdate(object? sender, FlightEventArgs e)
    {
        log.Info("MainMenu: Received flight update event.");

        if (mainTable.IsHandleCreated)
        {
            mainTable.BeginInvoke(() =>
            {
                var updatedFlights = _service.GetAllFlights().ToList();
                UpdateDataGridView(mainTable, updatedFlights);
            });
        }
    }
}
