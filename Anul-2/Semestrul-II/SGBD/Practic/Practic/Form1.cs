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

namespace Practic
{
    public partial class Form1 : Form
    {
        private DataSet dataSet = new DataSet();
        private SqlDataAdapter parentAdapter = new SqlDataAdapter();
        private SqlDataAdapter childAdapter = new SqlDataAdapter();

        private string connectionString = @"Server=VLAD\SQLEXPRESS;DataBase=Practic;Integrated Security = true; TrustServerCertificate=true";

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
                    parentAdapter = new SqlDataAdapter("SELECT * FROM Grupa", connection);
                    childAdapter = new SqlDataAdapter("SELECT * FROM Copil", connection);

                    parentAdapter.Fill(dataSet, "Grupa");
                    childAdapter.Fill(dataSet, "Copil");

                    DataColumn pkColumn = dataSet.Tables["Grupa"].Columns["idGrupa"];
                    DataColumn fkColumn = dataSet.Tables["Copil"].Columns["idGrupa"];

                    DataRelation relation = new DataRelation("FK_Grupa_Copil", pkColumn, fkColumn);

                    dataSet.Relations.Add(relation);

                    bindingParent.DataSource = dataSet.Tables["Grupa"];
                    grupaView.DataSource = bindingParent;

                    bindingChild.DataMember = "FK_Grupa_Copil";
                    bindingChild.DataSource = bindingParent;
                    copilView.DataSource = bindingChild;




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
                    numeBox.Text = selectedMed["Nume"].ToString();
                if (selectedMed.DataView.Table.Columns.Contains("Varsta"))
                    varstaBox.Text = selectedMed["Varsta"].ToString();
                if (selectedMed.DataView.Table.Columns.Contains("idGrupa"))
                    grupaBox.Text = selectedMed["idGrupa"].ToString();
            }
            else
            {
                numeBox.Clear();
                varstaBox.Clear();
                grupaBox.Clear();
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
                    if (dataSet.Tables.Contains("Copil"))
                    {
                        dataSet.Tables["Copil"].Clear();
                    }
                    if (dataSet.Tables.Contains("Grupa"))
                    {
                        dataSet.Tables["Grupa"].Clear();
                    }
                    parentAdapter.Fill(dataSet, "Grupa");
                    childAdapter.Fill(dataSet, "Copil");
                }
            }

            catch (Exception ex)
            {
                MessageBox.Show(ex.Message);
            }
        }

        private void addButton_Click(object sender, EventArgs e)
        {
            if (bindingParent.Current == null)
            {
                MessageBox.Show("Selectati o grupa inainte de a adauga un copil");
                return;
            }
            int codGrupa = (int)((DataRowView)bindingParent.Current)["idGrupa"];
            try
            {
                using (SqlConnection connection = new SqlConnection(connectionString))
                {
                    connection.Open();
                    SqlCommand command = new SqlCommand("INSERT INTO Copil(Nume,Varsta,idGrupa) VALUES(@nume,@varsta,@idGrupa)", connection);
                    command.Parameters.AddWithValue("@nume", numeBox.Text);
                    command.Parameters.AddWithValue("@varsta", varstaBox.Text);
                    command.Parameters.AddWithValue("@idGrupa",codGrupa);
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
            int copilID = (int)selectedMed["idCopil"];

            try
            {
                using (SqlConnection connection = new SqlConnection(connectionString))
                {
                    connection.Open();
                    SqlCommand command = new SqlCommand("DELETE FROM Copil where idCopil = @id", connection);
                    command.Parameters.AddWithValue("@id", copilID);
                    command.ExecuteNonQuery();
                }
                MessageBox.Show("Copilul a fost stears");
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
                MessageBox.Show("Selectati un copil inainte de a il actualiza!");
                return;

            }
            DataRowView selectedMed = (DataRowView)bindingChild.Current;
            int idCopil = (int)selectedMed["idCopil"];
            int idGrupa = (int)((DataRowView)bindingParent.Current)["idGrupa"];

            try
            {
                using (SqlConnection connection = new SqlConnection(connectionString))
                {
                    connection.Open();
                    SqlCommand command = new SqlCommand("UPDATE Copil SET Nume=@name,Varsta=@varsta,idGrupa=@grupaID where idCopil=@id", connection);
                    command.Parameters.AddWithValue("@name",numeBox.Text);
                    command.Parameters.AddWithValue("@varsta",varstaBox.Text);
                    command.Parameters.AddWithValue("@grupaID",idGrupa);
                    command.Parameters.AddWithValue("@id", idCopil);
                    command.ExecuteNonQuery();
                }
                MessageBox.Show("Copilul a fost actualizat!");
                incarcaTabel();
            }
            catch (Exception ex)
            {
                MessageBox.Show("Eroare" + ex.Message);
            }
        }

        private void label1_Click(object sender, EventArgs e)
        {

        }
    }
}
