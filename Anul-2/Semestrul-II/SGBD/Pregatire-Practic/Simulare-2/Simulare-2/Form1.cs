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
using static System.Windows.Forms.VisualStyles.VisualStyleElement.ListView;

namespace Simulare_2
{
    public partial class Form1 : Form
    {
        private DataSet dataSet = new DataSet();
        private SqlDataAdapter parentAdapter = new SqlDataAdapter();
        private SqlDataAdapter childAdapter = new SqlDataAdapter();

        private string connectionString = @"Server=VLAD\SQLEXPRESS;DataBase=Simulare2;Integrated Security = true; TrustServerCertificate=true";

        private BindingSource bindingParent = new BindingSource();
        private BindingSource bindingChild = new BindingSource();

        public Form1()
        {
            InitializeComponent();
        }

        private void addButton_Click(object sender, EventArgs e)
        {
            if (bindingParent.Current == null)
            {
                MessageBox.Show("Selectati un om inainte de a adauga un caine");
                return;
            }
            int codtaraClient = (int)((DataRowView)bindingParent.Current)["idOm"];
            try
            {
                using (SqlConnection connection = new SqlConnection(connectionString))
                {
                    connection.Open();
                    SqlCommand command = new SqlCommand("INSERT INTO Caine(Varsta,Nume,Rasa,idOm) VALUES (@varsta,@nume,@rasa,@idOm)", connection);
                    command.Parameters.AddWithValue("@varsta", varstaBox.Text);
                    command.Parameters.AddWithValue("@nume", numeBox.Text);
                    command.Parameters.AddWithValue("@rasa", rasaBox.Text);
                    command.Parameters.AddWithValue("@idOm", omBox.Text);
                    command.ExecuteNonQuery();
                }
                incarcaTabel();
            }
            catch (Exception ex)
            {
                MessageBox.Show("Eroare" + ex.Message);
            }

        }

        private void deleteButton_Click(object sender, EventArgs e)
        {
            if (bindingChild.Current == null)
            {
                MessageBox.Show("Selectati un client inainte de a il sterge");
                return;

            }
            DataRowView selectedMed = (DataRowView)bindingChild.Current;
            int clientID = (int)selectedMed["idCaine"];

            try
            {
                using (SqlConnection connection = new SqlConnection(connectionString))
                {
                    connection.Open();
                    SqlCommand command = new SqlCommand("DELETE FROM Caine where idCaine = @id", connection);
                    command.Parameters.AddWithValue("@id", clientID);
                    command.ExecuteNonQuery();
                }
                MessageBox.Show("Clientul a fost stears");
                incarcaTabel();
            }
            catch (Exception ex)
            {
                MessageBox.Show(ex.Message);
            }

        }

        private void updateButton_Click(object sender, EventArgs e)
        {
            if (bindingChild.Current == null)
            {
                MessageBox.Show("Selectati un client inainte de a il actualiza!");
                return;

            }
            DataRowView selectedMed = (DataRowView)bindingChild.Current;
            int id = (int)selectedMed["idCaine"];
            int idtaraclient = (int)((DataRowView)bindingParent.Current)["idOm"];

            try
            {
                using (SqlConnection connection = new SqlConnection(connectionString))
                {
                    connection.Open();
                    SqlCommand command = new SqlCommand("UPDATE Caine SET Varsta=@varsta,Nume=@nume,Rasa=@rasa,idOm=@idOm WHERE idCaine=@idCaine", connection);
                    command.Parameters.AddWithValue("@varsta", varstaBox.Text);
                    command.Parameters.AddWithValue("@rasa", rasaBox.Text);
                    command.Parameters.AddWithValue("@nume", numeBox.Text);
                    command.Parameters.AddWithValue("@idOm", idtaraclient);
                    command.Parameters.AddWithValue("@idCaine", id);
                    command.ExecuteNonQuery();
                }
                MessageBox.Show("Clientul a fost actualizat!");
                incarcaTabel();
            }
            catch (Exception ex)
            {
                MessageBox.Show("Eroare" + ex.Message);
            }

        }
        private void bindingChild_CurrentChanged(object sender, EventArgs e)
        {
            if (bindingChild.Current != null)
            {
                DataRowView selectedMed = (DataRowView)bindingChild.Current;
                if (selectedMed.DataView.Table.Columns.Contains("Varsta"))
                    varstaBox.Text = selectedMed["Varsta"].ToString();
                if (selectedMed.DataView.Table.Columns.Contains("Nume"))
                    numeBox.Text = selectedMed["Nume"].ToString();
                if (selectedMed.DataView.Table.Columns.Contains("Rasa"))
                    rasaBox.Text = selectedMed["Rasa"].ToString();
                if (selectedMed.DataView.Table.Columns.Contains("idOm"))
                    omBox.Text = selectedMed["idOm"].ToString();
            }
            else
            {
                varstaBox.Clear();
                numeBox.Clear();
                rasaBox.Clear();
                omBox.Clear();
            }

        }

        private void incarcaTabel()
        {
            try
            {
                using (SqlConnection connection = new SqlConnection(connectionString))
                {
                    parentAdapter.SelectCommand.Connection = connection;
                    childAdapter.SelectCommand.Connection = connection;
                    if (dataSet.Tables.Contains("Caine"))
                    {
                        dataSet.Tables["Caine"].Clear();
                    }
                    if (dataSet.Tables.Contains("Om"))
                    {
                        dataSet.Tables["Om"].Clear();
                    }
                    parentAdapter.Fill(dataSet, "Om");
                    childAdapter.Fill(dataSet, "Caine");
                }
            }

            catch (Exception ex)
            {
                MessageBox.Show(ex.Message);
            }
        }

        private void Form1_Load(object sender, EventArgs e)
        {
            try
            {
                using (SqlConnection connection = new SqlConnection(connectionString))
                {
                    connection.Open();
                    parentAdapter = new SqlDataAdapter("SELECT * FROM Om", connection);
                    childAdapter = new SqlDataAdapter("SELECT * FROM Caine", connection);

                    parentAdapter.Fill(dataSet, "Om");
                    childAdapter.Fill(dataSet, "Caine");

                    DataColumn pkColumn = dataSet.Tables["Om"].Columns["idOm"];
                    DataColumn fkColumn = dataSet.Tables["Caine"].Columns["idOm"];

                    DataRelation relation = new DataRelation("FK_Om_Caine", pkColumn, fkColumn);

                    dataSet.Relations.Add(relation);

                    bindingParent.DataSource = dataSet.Tables["Om"];
                   omView.DataSource = bindingParent;

                    bindingChild.DataMember = "FK_Om_Caine";
                    bindingChild.DataSource = bindingParent;
                    caineView.DataSource = bindingChild;


                }
            }
            catch (Exception ex)
            {
                MessageBox.Show(ex.Message);

            }

        }
    }
}
