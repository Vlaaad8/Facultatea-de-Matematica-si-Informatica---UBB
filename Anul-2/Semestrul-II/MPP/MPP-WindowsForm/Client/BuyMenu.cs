using DefaultNamespace;
using MPP_CSharpProject.domain;
using Services;

namespace MPP_WindowsForm;

public partial class BuyMenu : Form
{
    private IServices _service;
    private BindingSource bindingSource;
    private String origin;
    private String departure;
    private DateTime date;

    public BuyMenu(IServices service, String origin, String departure, DateTime date)
    {
        this._service = service;
        this.origin = origin;
        this.departure = departure;
        this.date = date;
        bindingSource = new BindingSource();
        InitializeComponent();
        SetupComponents();
        LoadData();
    }

    private void SetupComponents()
    {
        dataGridView1.AutoGenerateColumns = false;

        dataGridView1.Columns.Add(
            new DataGridViewTextBoxColumn
            {
                HeaderText = "Origin",
                DataPropertyName = "origin",
                Name = "originColumn",
                CellTemplate = new DataGridViewTextBoxCell()
            });

        dataGridView1.Columns.Add(
            new DataGridViewTextBoxColumn
            {
                HeaderText = "Departure",
                DataPropertyName = "departure",
                Name = "departureColumn",
                CellTemplate = new DataGridViewTextBoxCell()
            });

        dataGridView1.Columns.Add(
            new DataGridViewTextBoxColumn
            {
                HeaderText = "Airport",
                DataPropertyName = "airport",
                Name = "airportColumn",
                CellTemplate = new DataGridViewTextBoxCell()
            });
        dataGridView1.Columns.Add(
            new DataGridViewTextBoxColumn
            {
                HeaderText = "Date",
                DataPropertyName = "dayTime",
                Name = "dayTimeColumn",
                CellTemplate = new DataGridViewTextBoxCell()
            });
        dataGridView1.Columns.Add(
            new DataGridViewTextBoxColumn
            {
                HeaderText = "Available Seats",
                DataPropertyName = "availableSeats",
                Name = "seatsColumn",
                CellTemplate = new DataGridViewTextBoxCell()
            });
    }

    private void LoadData()
    {
        //bindingSource = new BindingSource();
        Flight flight = new Flight(origin, departure, 0, "", date);
        flight.SetId(0);
        bindingSource.DataSource = _service.SearchFlight(flight);
        dataGridView1.DataSource = bindingSource;
    }

    private void button1_Click(object sender, EventArgs e)
    {
    }

    private void buyButton_Click(object sender, EventArgs e)
    {
        if (dataGridView1.SelectedRows.Count > 0)
        {
            DataGridViewRow selectedRow = dataGridView1.SelectedRows[0];
            Flight selectedFlight = selectedRow.DataBoundItem as Flight;
            String buyers = buyersBox.Text;
            int numberOfTickets = (int)numberBox.Value;
            Ticket ticket = new Ticket(buyers, selectedFlight, numberOfTickets);
            ticket.SetId(0);
            _service.AddTicket(ticket);
            MessageBox.Show("Tickets was succesfully bought!");
        }
    }

    private void goBackButton_Click(object sender, EventArgs e)
    {

    }
}