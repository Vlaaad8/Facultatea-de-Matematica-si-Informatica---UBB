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

namespace Simulare_1
{
    public partial class Form1 : Form
    {
        private DataSet dataSet = new DataSet();
        private SqlDataAdapter parentAdapter = new SqlDataAdapter();
        private SqlDataAdapter childAdapter = new SqlDataAdapter();

        private string connectionString = @"Server=VLAD\SQLEXPRESS;DataBase=Simualare;Integrated Security = true; TrustServerCertificate=true";

        private BindingSource bindingParent = new BindingSource();
        private BindingSource bindingChild = new BindingSource();


        public Form1()
        {
            InitializeComponent();
        }

        private void bindingChild_CurrentChanged(object sender, EventArgs e)
        {
            if (bindingChild.Current != null)
            {
                DataRowView selectedMed = (DataRowView)bindingChild.Current;
                if (selectedMed.DataView.Table.Columns.Contains("Titlu"))
                    titleBox.Text = selectedMed["Titlu"].ToString();
                if (selectedMed.DataView.Table.Columns.Contains("DataLansarii"))
                    dateBox.Text = selectedMed["DataLansarii"].ToString();
                if (selectedMed.DataView.Table.Columns.Contains("idAutor"))
                    idAutorBox.Text = selectedMed["idAutor"].ToString();
            }
            else
            {
                titleBox.Clear();
                idAutorBox.Clear();
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
                    if (dataSet.Tables.Contains("Carte"))
                    {
                        dataSet.Tables["Carte"].Clear();
                    }
                    if (dataSet.Tables.Contains("Autor"))
                    {
                        dataSet.Tables["Autor"].Clear();
                    }
                    parentAdapter.Fill(dataSet, "Autor");
                    childAdapter.Fill(dataSet, "Carte");
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
                MessageBox.Show("Selectati un autor inainte de a adauga o carte");
                return;
            }
            int codtaraClient = (int)((DataRowView)bindingParent.Current)["idAutor"];
            try
            {
                using (SqlConnection connection = new SqlConnection(connectionString))
                {
                    connection.Open();
                    SqlCommand command = new SqlCommand("INSERT INTO Carte(Titlu,DataLansarii,idAutor) VALUES (@nume,@dataLansarii,@idAutor)", connection);
                    command.Parameters.AddWithValue("@nume", titleBox.Text);
                    command.Parameters.AddWithValue("@dataLansarii", dateBox.Value);
                    command.Parameters.AddWithValue("@idAutor", codtaraClient);
                    command.ExecuteNonQuery();
                }
                incarcaTabel();
            }
            catch (Exception ex)
            {
                MessageBox.Show("Eroare" + ex.Message);
            }

        }

        private void stergereButton_Click(object sender, EventArgs e)
        {
            if (bindingChild.Current == null)
            {
                MessageBox.Show("Selectati o carte inainte de a il sterge");
                return;

            }
            DataRowView selectedMed = (DataRowView)bindingChild.Current;
            int clientID = (int)selectedMed["idCarte"];

            try
            {
                using (SqlConnection connection = new SqlConnection(connectionString))
                {
                    connection.Open();
                    SqlCommand command = new SqlCommand("DELETE FROM Carte where idCarte = @id", connection);
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
                MessageBox.Show("Selectatio carte inainte de a il actualiza!");
                return;

            }
            DataRowView selectedMed = (DataRowView)bindingChild.Current;
            int idCarte = (int)selectedMed["idCarte"];
            int idAutor = (int)((DataRowView)bindingParent.Current)["idAutor"];

            try
            {
                using (SqlConnection connection = new SqlConnection(connectionString))
                {
                    connection.Open();
                    SqlCommand command = new SqlCommand("UPDATE Carte SET Titlu=@titlu,DataLansarii=@datalansarii,idAutor=@idAutor where idCarte=@id", connection);
                    command.Parameters.AddWithValue("@titlu", titleBox.Text);
                    command.Parameters.AddWithValue("@datalansarii", dateBox.Value);
                    command.Parameters.AddWithValue("@idAutor", idAutor);
                    command.Parameters.AddWithValue("@id", idCarte);
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
    

        private void Form1_Load_1(object sender, EventArgs e)
        {
            try
            {
                using (SqlConnection connection = new SqlConnection(connectionString))
                {
                    connection.Open();
                    parentAdapter = new SqlDataAdapter("SELECT * FROM Autor", connection);
                    childAdapter = new SqlDataAdapter("SELECT * FROM Carte", connection);

                    parentAdapter.Fill(dataSet, "Autor");
                    childAdapter.Fill(dataSet, "Carte");

                    DataColumn pkColumn = dataSet.Tables["Autor"].Columns["idAutor"];
                    DataColumn fkColumn = dataSet.Tables["Carte"].Columns["idAutor"];

                    DataRelation relation = new DataRelation("FK_Autor_Carte", pkColumn, fkColumn);

                    dataSet.Relations.Add(relation);

                    bindingParent.DataSource = dataSet.Tables["Autor"];
                    AutorView.DataSource = bindingParent;

                    bindingChild.DataMember = "FK_Autor_Carte";
                    bindingChild.DataSource = bindingParent;
                    CarteView.DataSource = bindingChild;





                }
            }
            catch (Exception ex)
            {
                MessageBox.Show(ex.Message);

            }
        }
    }
}
