using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Data.SqlClient;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace Laborator_1
{
    public partial class Form1 : Form
    {
        private DataSet dataSet = new DataSet();
        private SqlDataAdapter parentAdapter = new SqlDataAdapter();
        private SqlDataAdapter childAdapter = new SqlDataAdapter();

        private string connectionString = @"Server=VLAD\SQLEXPRESS;DataBase=Fabrica;Integrated Security = true; TrustServerCertificate=true";

        private BindingSource bindingParent = new BindingSource();
        private BindingSource bindingChild = new BindingSource();


        public Form1()
        {
            InitializeComponent();
        }

        private void Form1_Load(object sender, EventArgs e)
        {

            try
            {
                using (SqlConnection connection = new SqlConnection(connectionString))
                {
                    connection.Open();
                    parentAdapter = new SqlDataAdapter("SELECT * FROM FabricaMedicamente", connection);
                    childAdapter = new SqlDataAdapter("SELECT * FROM Medicament", connection);

                    parentAdapter.Fill(dataSet, "FabricaMedicamente");
                    childAdapter.Fill(dataSet, "Medicament");

                    DataColumn pkColumn = dataSet.Tables["FabricaMedicamente"].Columns["IDFabrica"];
                    DataColumn fkColumn = dataSet.Tables["Medicament"].Columns["IDFabrica"];

                    DataRelation relation = new DataRelation("FK_Fabrica_Medicamente", pkColumn, fkColumn);

                    dataSet.Relations.Add(relation);

                    bindingParent.DataSource = dataSet.Tables["FabricaMedicamente"];
                    FabricaView.DataSource = bindingParent;

                    bindingChild.DataMember = "FK_Fabrica_Medicamente";
                    bindingChild.DataSource = bindingParent;
                    MedicamenteView.DataSource = bindingChild;

                    numeFabricaBox.DataBindings.Add("Text", bindingParent, "Nume", true);



                }
            }
            catch (Exception ex)
            {
                MessageBox.Show(ex.Message);

            }


        }

        private void button4_Click(object sender, EventArgs e)
        {
            try
            {
                using (SqlConnection connection = new SqlConnection(connectionString))
                {
                    parentAdapter.SelectCommand.Connection = connection;
                    childAdapter.SelectCommand.Connection = connection;
                    if (dataSet.Tables.Contains("Medicament"))
                    {
                        dataSet.Tables["Medicament"].Clear();
                    }
                    if (dataSet.Tables.Contains("FabricaMedicamente"))
                    {
                        dataSet.Tables["FabricaMedicamente"].Clear();
                    }
                    parentAdapter.Fill(dataSet, "FabricaMedicamente");
                    childAdapter.Fill(dataSet, "Medicament");
                }
            }
            catch (Exception ex)
            {
                MessageBox.Show(ex.Message);
            }
        }
        private void bindingChild_CurrentChanged(object sender, EventArgs e)
        {
            if (bindingChild.Current != null)
            {
                DataRowView selectedMed = (DataRowView)bindingChild.Current;
                if (selectedMed.DataView.Table.Columns.Contains("Nume"))
                    numeMedicamentBox.Text = selectedMed["Nume"].ToString();
                if (selectedMed.DataView.Table.Columns.Contains("Tip"))
                    tipMedicamentBox.Text = selectedMed["Tip"].ToString();
                if (selectedMed.DataView.Table.Columns.Contains("Pret"))
                    pretMedicamentBox.Text = selectedMed["Pret"].ToString();
                if (selectedMed.DataView.Table.Columns.Contains("IDFabrica"))
                    idFabricaBox.Text = selectedMed["IDFabrica"].ToString();
            }
            else
            {
                tipMedicamentBox.Clear();
                pretMedicamentBox.Clear();
                idFabricaBox.Clear();
                numeMedicamentBox.Clear();
            }

        }
        private void textBox1_TextChanged(object sender, EventArgs e)
        {

        }

        private void label4_Click(object sender, EventArgs e)
        {

        }

        private void label5_Click(object sender, EventArgs e)
        {

        }

        private void FabricaView_CellContentClick(object sender, DataGridViewCellEventArgs e)
        {

        }

        private void button2_Click(object sender, EventArgs e)
        {
            if (bindingParent.Current == null)
            {
                MessageBox.Show("Selectati o fabrica inainte de a adauga un medicament");
                return;
            }
            int idFabrica = (int)((DataRowView)bindingParent.Current)["IDFabrica"];
            int price;
            if (!int.TryParse(pretMedicamentBox.Text, out price))
            {
                MessageBox.Show("Date invalide! Reintroduceti!");
                return;
            }
            try
            {
                using (SqlConnection connection = new SqlConnection(connectionString))
                {
                    connection.Open();
                    SqlCommand command = new SqlCommand("INSERT INTO Medicament(Nume,Tip,Pret,IDFabrica) VALUES (@nume,@tip,@pret,@idfabrica)", connection);
                    command.Parameters.AddWithValue("@nume", numeMedicamentBox.Text);
                    command.Parameters.AddWithValue("@tip", tipMedicamentBox.Text);
                    command.Parameters.AddWithValue("@pret", price);
                    command.Parameters.AddWithValue("@idfabrica", idFabrica);
                    command.ExecuteNonQuery();
                }
                button4_Click(null, null);
            }
            catch (Exception ex)
            {
                MessageBox.Show("Eroare" + ex.Message);
            }

        }

        private void button3_Click(object sender, EventArgs e)
        {
            if (bindingChild.Current == null)
            {
                MessageBox.Show("Selectati un medicament inainte de a il actualiza!");
                return;

            }
            DataRowView selectedMed = (DataRowView)bindingChild.Current;
            int id = (int)selectedMed["IDMedicament"];
            int idFabrica = (int)((DataRowView)bindingParent.Current)["IDFabrica"];
            int price;
            if (!int.TryParse(pretMedicamentBox.Text, out price))
            {
                MessageBox.Show("Date invalide! Reintroduceti!");
                return;
            }
            try
            {
                using (SqlConnection connection = new SqlConnection(connectionString))
                {
                    connection.Open();
                    SqlCommand command = new SqlCommand("UPDATE Medicament SET Nume=@nume,Tip=@tip,Pret=@pret,IDFabrica=@idfabrica where IDMedicament=@id", connection);
                    command.Parameters.AddWithValue("@nume", numeMedicamentBox.Text);
                    command.Parameters.AddWithValue("@tip", tipMedicamentBox.Text);
                    command.Parameters.AddWithValue("@pret", price);
                    command.Parameters.AddWithValue("@idfabrica", idFabrica);
                    command.Parameters.AddWithValue("@id", id);
                    command.ExecuteNonQuery();
                }
                MessageBox.Show("Medicamentul a fost actualizat!");
                button4_Click(null, null);
            }
            catch (Exception ex)
            {
                MessageBox.Show("Eroare" + ex.Message);
            }

        }

        private void button1_Click(object sender, EventArgs e)
        {
            if (bindingChild.Current == null)
            {
                MessageBox.Show("Selectati un medicament inainte de a il sterge");
                return;

            }
            DataRowView selectedMed = (DataRowView)bindingChild.Current;
            int medID = (int)selectedMed["IDMedicament"];

            try
            {
                using (SqlConnection connection = new SqlConnection(connectionString))
                {
                    connection.Open();
                    SqlCommand command = new SqlCommand("DELETE FROM Medicament where IDMedicament = @id",connection);
                    command.Parameters.AddWithValue("@id", medID);
                    command.ExecuteNonQuery();
                }
                MessageBox.Show("Cartea a fost stearsa");
                button4_Click(null, null);
            }
            catch (Exception ex)
            {
                MessageBox.Show(ex.Message);
            }

        }
    }
}
