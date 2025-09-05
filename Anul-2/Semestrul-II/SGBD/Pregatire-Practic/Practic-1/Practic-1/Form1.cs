using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Data.SqlClient;
using System.Drawing;
using System.Linq;
using System.Reflection;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace Practic_1
{
    public partial class Form1 : Form
    {
        private DataSet dataSet = new DataSet();
        private SqlDataAdapter parentAdapter = new SqlDataAdapter();
        private SqlDataAdapter childAdapter = new SqlDataAdapter();

        private string connectionString = @"Server=VLAD\SQLEXPRESS;DataBase=Problema1;Integrated Security = true; TrustServerCertificate=true";

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
                    parentAdapter = new SqlDataAdapter("SELECT * FROM Tari", connection);
                    childAdapter = new SqlDataAdapter("SELECT * FROM Clienti", connection);

                    parentAdapter.Fill(dataSet, "Tari");
                    childAdapter.Fill(dataSet, "Clienti");

                    DataColumn pkColumn = dataSet.Tables["Tari"].Columns["cod_tara"];
                    DataColumn fkColumn = dataSet.Tables["Clienti"].Columns["cod_tara"];

                    DataRelation relation = new DataRelation("FK_Tari_Clienti", pkColumn, fkColumn);

                    dataSet.Relations.Add(relation);

                    bindingParent.DataSource = dataSet.Tables["Tari"];
                    taraView.DataSource = bindingParent;

                    bindingChild.DataMember = "FK_Tari_Clienti";
                    bindingChild.DataSource = bindingParent;
                    clientView.DataSource = bindingChild;




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
                if (selectedMed.DataView.Table.Columns.Contains("nume"))
                   nume.Text = selectedMed["nume"].ToString();
                if (selectedMed.DataView.Table.Columns.Contains("email"))
                    email.Text = selectedMed["email"].ToString();
                if (selectedMed.DataView.Table.Columns.Contains("data_nasterii"))
                    data_nasterii.Text = selectedMed["data_nasterii"].ToString();
                if (selectedMed.DataView.Table.Columns.Contains("cod_tara"))
                    cod_tara.Text = selectedMed["cod_tara"].ToString();
            }
            else
            {
               nume.Clear();
                email.Clear();
               // data_nasterii.
                cod_tara.Clear();
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
                    if (dataSet.Tables.Contains("Clienti"))
                    {
                        dataSet.Tables["Clienti"].Clear();
                    }
                    if (dataSet.Tables.Contains("Tari"))
                    {
                        dataSet.Tables["Tari"].Clear();
                    }
                    parentAdapter.Fill(dataSet, "Tari");
                    childAdapter.Fill(dataSet, "Clienti");
                }
            }

            catch (Exception ex)
            {
                MessageBox.Show(ex.Message);
            }
}
        private void button1_Click(object sender, EventArgs e)
        {
            if (bindingParent.Current == null)
            {
                MessageBox.Show("Selectati o tara inainte de a adauga un client");
                return;
            }
            int codtaraClient = (int)((DataRowView)bindingParent.Current)["cod_tara"];
            try
            {
                using (SqlConnection connection = new SqlConnection(connectionString))
                {
                    connection.Open();
                    SqlCommand command = new SqlCommand("INSERT INTO Clienti(nume,email,data_nasterii,cod_tara) VALUES (@nume,@email,@data_nasterii,@cod_tara)", connection);
                    command.Parameters.AddWithValue("@nume", nume.Text);
                    command.Parameters.AddWithValue("@email", email.Text);
                    command.Parameters.AddWithValue("@data_nasterii", data_nasterii.Value);
                    command.Parameters.AddWithValue("@cod_tara", codtaraClient);
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
                MessageBox.Show("Selectati un copil inainte de a il sterge");
                return;

            }
            DataRowView selectedMed = (DataRowView)bindingChild.Current;
            int clientID = (int)selectedMed["cod_client"];

            try
            {
                using (SqlConnection connection = new SqlConnection(connectionString))
                {
                    connection.Open();
                    SqlCommand command = new SqlCommand("DELETE FROM Clienti where cod_client = @id", connection);
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
            int id = (int)selectedMed["cod_client"];
            int idtaraclient = (int)((DataRowView)bindingParent.Current)["cod_tara"];
          
            try
            {
                using (SqlConnection connection = new SqlConnection(connectionString))
                {
                    connection.Open();
                    SqlCommand command = new SqlCommand("UPDATE Clienti SET nume=@nume,email=@email,data_nasterii=@data_nasterii,cod_tara=@cod_tara where cod_client=@id", connection);
                    command.Parameters.AddWithValue("@nume", nume.Text);
                    command.Parameters.AddWithValue("@email", email.Text);
                    command.Parameters.AddWithValue("@data_nasterii", data_nasterii.Value);
                    command.Parameters.AddWithValue("@cod_tara",idtaraclient);
                    command.Parameters.AddWithValue("@id", id);
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
    }
}
