using System.Windows.Forms;

namespace Laborator_1
{
    partial class Form1
    {
        /// <summary>
        /// Required designer variable.
        /// </summary>
        private System.ComponentModel.IContainer components = null;

        /// <summary>
        /// Clean up any resources being used.
        /// </summary>
        /// <param name="disposing">true if managed resources should be disposed; otherwise, false.</param>
        protected override void Dispose(bool disposing)
        {
            if (disposing && (components != null))
            {
                components.Dispose();
            }
            base.Dispose(disposing);
        }

        #region Windows Form Designer generated code

        /// <summary>
        /// Required method for Designer support - do not modify
        /// the contents of this method with the code editor.
        /// </summary>
        private void InitializeComponent()
        {
            this.bindingChild = new BindingSource();
            this.components = new System.ComponentModel.Container();
            this.FabricaView = new System.Windows.Forms.DataGridView();
            this.fabricaMedicamenteBindingSource = new System.Windows.Forms.BindingSource(this.components);
            this.fabricaDataSet1 = new Laborator_1.FabricaDataSet1();
            this.button1 = new System.Windows.Forms.Button();
            this.button2 = new System.Windows.Forms.Button();
            this.button3 = new System.Windows.Forms.Button();
            this.numeMedicamentBox = new System.Windows.Forms.TextBox();
            this.pretMedicamentBox = new System.Windows.Forms.TextBox();
            this.tipMedicamentBox = new System.Windows.Forms.TextBox();
            this.MedicamenteView = new System.Windows.Forms.DataGridView();
            this.medicamentBindingSource = new System.Windows.Forms.BindingSource(this.components);
            this.medicamentTableAdapter = new Laborator_1.FabricaDataSet1TableAdapters.MedicamentTableAdapter();
            this.fabricaMedicamenteTableAdapter = new Laborator_1.FabricaDataSet1TableAdapters.FabricaMedicamenteTableAdapter();
            this.label1 = new System.Windows.Forms.Label();
            this.label2 = new System.Windows.Forms.Label();
            this.label3 = new System.Windows.Forms.Label();
            this.button4 = new System.Windows.Forms.Button();
            this.numeFabricaBox = new System.Windows.Forms.TextBox();
            this.idFabricaBox = new System.Windows.Forms.TextBox();
            this.label4 = new System.Windows.Forms.Label();
            ((System.ComponentModel.ISupportInitialize)(this.FabricaView)).BeginInit();
            ((System.ComponentModel.ISupportInitialize)(this.fabricaMedicamenteBindingSource)).BeginInit();
            ((System.ComponentModel.ISupportInitialize)(this.fabricaDataSet1)).BeginInit();
            ((System.ComponentModel.ISupportInitialize)(this.MedicamenteView)).BeginInit();
            ((System.ComponentModel.ISupportInitialize)(this.medicamentBindingSource)).BeginInit();
            this.SuspendLayout();
            // 
            // FabricaView
            // 
            this.FabricaView.ColumnHeadersHeightSizeMode = System.Windows.Forms.DataGridViewColumnHeadersHeightSizeMode.AutoSize;
            this.FabricaView.Location = new System.Drawing.Point(36, 54);
            this.FabricaView.Name = "FabricaView";
            this.FabricaView.RowHeadersWidth = 51;
            this.FabricaView.RowTemplate.Height = 24;
            this.FabricaView.Size = new System.Drawing.Size(524, 236);
            this.FabricaView.TabIndex = 0;
            this.FabricaView.CellContentClick += new System.Windows.Forms.DataGridViewCellEventHandler(this.FabricaView_CellContentClick);
            // 
            // fabricaMedicamenteBindingSource
            // 
            this.fabricaMedicamenteBindingSource.DataMember = "FabricaMedicamente";
            this.fabricaMedicamenteBindingSource.DataSource = this.fabricaDataSet1;
            // 
            // fabricaDataSet1
            // 
            this.fabricaDataSet1.DataSetName = "FabricaDataSet1";
            this.fabricaDataSet1.SchemaSerializationMode = System.Data.SchemaSerializationMode.IncludeSchema;
            // 
            // button1
            // 
            this.button1.Location = new System.Drawing.Point(748, 357);
            this.button1.Name = "button1";
            this.button1.Size = new System.Drawing.Size(170, 23);
            this.button1.TabIndex = 1;
            this.button1.Text = "Delete";
            this.button1.UseVisualStyleBackColor = true;
            this.button1.Click += new System.EventHandler(this.button1_Click);
            // 
            // button2
            // 
            this.button2.Location = new System.Drawing.Point(378, 480);
            this.button2.Name = "button2";
            this.button2.Size = new System.Drawing.Size(170, 23);
            this.button2.TabIndex = 2;
            this.button2.Text = "Add";
            this.button2.UseVisualStyleBackColor = true;
            this.button2.Click += new System.EventHandler(this.button2_Click);
            // 
            // button3
            // 
            this.button3.Location = new System.Drawing.Point(748, 318);
            this.button3.Name = "button3";
            this.button3.Size = new System.Drawing.Size(170, 23);
            this.button3.TabIndex = 3;
            this.button3.Text = "Update";
            this.button3.UseVisualStyleBackColor = true;
            this.button3.Click += new System.EventHandler(this.button3_Click);
            // 
            // numeMedicamentBox
            // 
            this.numeMedicamentBox.Location = new System.Drawing.Point(378, 312);
            this.numeMedicamentBox.Name = "numeMedicamentBox";
            this.numeMedicamentBox.Size = new System.Drawing.Size(170, 22);
            this.numeMedicamentBox.TabIndex = 4;
            // 
            // pretMedicamentBox
            // 
            this.pretMedicamentBox.Location = new System.Drawing.Point(378, 405);
            this.pretMedicamentBox.Name = "pretMedicamentBox";
            this.pretMedicamentBox.Size = new System.Drawing.Size(170, 22);
            this.pretMedicamentBox.TabIndex = 5;
            // 
            // tipMedicamentBox
            // 
            this.tipMedicamentBox.Location = new System.Drawing.Point(378, 358);
            this.tipMedicamentBox.Name = "tipMedicamentBox";
            this.tipMedicamentBox.Size = new System.Drawing.Size(170, 22);
            this.tipMedicamentBox.TabIndex = 6;
            // 
            // MedicamenteView
            // 
            this.MedicamenteView.ColumnHeadersHeightSizeMode = System.Windows.Forms.DataGridViewColumnHeadersHeightSizeMode.AutoSize;
            this.MedicamenteView.Location = new System.Drawing.Point(636, 54);
            this.MedicamenteView.Name = "MedicamenteView";
            this.MedicamenteView.RowHeadersWidth = 51;
            this.MedicamenteView.RowTemplate.Height = 24;
            this.MedicamenteView.Size = new System.Drawing.Size(482, 236);
            this.MedicamenteView.TabIndex = 7;
            // 
            // medicamentBindingSource
            // 
            this.medicamentBindingSource.DataMember = "Medicament";
            this.medicamentBindingSource.DataSource = this.fabricaDataSet1;
            // 
            // medicamentTableAdapter
            // 
            this.medicamentTableAdapter.ClearBeforeFill = true;
            // 
            // fabricaMedicamenteTableAdapter
            // 
            this.fabricaMedicamenteTableAdapter.ClearBeforeFill = true;
            // 
            // label1
            // 
            this.label1.AutoSize = true;
            this.label1.Location = new System.Drawing.Point(329, 312);
            this.label1.Name = "label1";
            this.label1.Size = new System.Drawing.Size(43, 16);
            this.label1.TabIndex = 8;
            this.label1.Text = "Nume";
            // 
            // label2
            // 
            this.label2.AutoSize = true;
            this.label2.Location = new System.Drawing.Point(332, 363);
            this.label2.Name = "label2";
            this.label2.Size = new System.Drawing.Size(27, 16);
            this.label2.TabIndex = 9;
            this.label2.Text = "Tip";
            // 
            // label3
            // 
            this.label3.AutoSize = true;
            this.label3.Location = new System.Drawing.Point(332, 405);
            this.label3.Name = "label3";
            this.label3.Size = new System.Drawing.Size(31, 16);
            this.label3.TabIndex = 10;
            this.label3.Text = "Pret";
            // 
            // button4
            // 
            this.button4.Location = new System.Drawing.Point(748, 403);
            this.button4.Name = "button4";
            this.button4.Size = new System.Drawing.Size(170, 23);
            this.button4.TabIndex = 11;
            this.button4.Text = "Refresh";
            this.button4.UseVisualStyleBackColor = true;
            this.button4.Click += new System.EventHandler(this.button4_Click);
            // 
            // numeFabricaBox
            // 
            this.numeFabricaBox.Location = new System.Drawing.Point(36, 342);
            this.numeFabricaBox.Name = "numeFabricaBox";
            this.numeFabricaBox.Size = new System.Drawing.Size(180, 22);
            this.numeFabricaBox.TabIndex = 12;
            // 
            // idFabricaBox
            // 
            this.idFabricaBox.Location = new System.Drawing.Point(378, 437);
            this.idFabricaBox.Name = "idFabricaBox";
            this.idFabricaBox.Size = new System.Drawing.Size(170, 22);
            this.idFabricaBox.TabIndex = 13;
            this.idFabricaBox.TextChanged += new System.EventHandler(this.textBox1_TextChanged);
            // 
            // label4
            // 
            this.label4.AutoSize = true;
            this.label4.Location = new System.Drawing.Point(306, 443);
            this.label4.Name = "label4";
            this.label4.Size = new System.Drawing.Size(66, 16);
            this.label4.TabIndex = 14;
            this.label4.Text = "IDFabrica";
            this.label4.Click += new System.EventHandler(this.label4_Click);
            // 
            // Form1
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(8F, 16F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.ClientSize = new System.Drawing.Size(1173, 515);
            this.Controls.Add(this.label4);
            this.Controls.Add(this.idFabricaBox);
            this.Controls.Add(this.numeFabricaBox);
            this.Controls.Add(this.button4);
            this.Controls.Add(this.label3);
            this.Controls.Add(this.label2);
            this.Controls.Add(this.label1);
            this.Controls.Add(this.MedicamenteView);
            this.Controls.Add(this.tipMedicamentBox);
            this.Controls.Add(this.pretMedicamentBox);
            this.Controls.Add(this.numeMedicamentBox);
            this.Controls.Add(this.button3);
            this.Controls.Add(this.button2);
            this.Controls.Add(this.button1);
            this.Controls.Add(this.FabricaView);
            this.Name = "Form1";
            this.Text = "Form1";
            this.Load += new System.EventHandler(this.Form1_Load);
            this.bindingChild.CurrentChanged += new System.EventHandler(this.bindingChild_CurrentChanged);
            ((System.ComponentModel.ISupportInitialize)(this.FabricaView)).EndInit();
            ((System.ComponentModel.ISupportInitialize)(this.fabricaMedicamenteBindingSource)).EndInit();
            ((System.ComponentModel.ISupportInitialize)(this.fabricaDataSet1)).EndInit();
            ((System.ComponentModel.ISupportInitialize)(this.MedicamenteView)).EndInit();
            ((System.ComponentModel.ISupportInitialize)(this.medicamentBindingSource)).EndInit();
            this.ResumeLayout(false);
            this.PerformLayout();

        }

        #endregion

        private System.Windows.Forms.DataGridView FabricaView;
        private System.Windows.Forms.Button button1;
        private System.Windows.Forms.Button button2;
        private System.Windows.Forms.Button button3;
        private System.Windows.Forms.TextBox numeMedicamentBox;
        private System.Windows.Forms.TextBox pretMedicamentBox;
        private System.Windows.Forms.TextBox tipMedicamentBox;
        private System.Windows.Forms.DataGridView MedicamenteView;
        private FabricaDataSet1 fabricaDataSet1;
        private System.Windows.Forms.BindingSource medicamentBindingSource;
        private FabricaDataSet1TableAdapters.MedicamentTableAdapter medicamentTableAdapter;
        private System.Windows.Forms.BindingSource fabricaMedicamenteBindingSource;
        private FabricaDataSet1TableAdapters.FabricaMedicamenteTableAdapter fabricaMedicamenteTableAdapter;
        private System.Windows.Forms.Label label1;
        private System.Windows.Forms.Label label2;
        private System.Windows.Forms.Label label3;
        private System.Windows.Forms.Button button4;
        private System.Windows.Forms.TextBox numeFabricaBox;
        private System.Windows.Forms.TextBox idFabricaBox;
        private System.Windows.Forms.Label label4;
    }
}

