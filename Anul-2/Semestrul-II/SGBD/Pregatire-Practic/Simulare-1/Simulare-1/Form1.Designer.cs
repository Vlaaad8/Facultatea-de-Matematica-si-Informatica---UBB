namespace Simulare_1
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
            this.AutorView = new System.Windows.Forms.DataGridView();
            this.CarteView = new System.Windows.Forms.DataGridView();
            this.titleBox = new System.Windows.Forms.TextBox();
            this.dateBox = new System.Windows.Forms.DateTimePicker();
            this.idAutorBox = new System.Windows.Forms.TextBox();
            this.button1 = new System.Windows.Forms.Button();
            this.stergereButton = new System.Windows.Forms.Button();
            this.updateButton = new System.Windows.Forms.Button();
            ((System.ComponentModel.ISupportInitialize)(this.AutorView)).BeginInit();
            ((System.ComponentModel.ISupportInitialize)(this.CarteView)).BeginInit();
            this.SuspendLayout();
            // 
            // AutorView
            // 
            this.AutorView.ColumnHeadersHeightSizeMode = System.Windows.Forms.DataGridViewColumnHeadersHeightSizeMode.AutoSize;
            this.AutorView.Location = new System.Drawing.Point(59, 39);
            this.AutorView.Name = "AutorView";
            this.AutorView.RowHeadersWidth = 51;
            this.AutorView.RowTemplate.Height = 24;
            this.AutorView.Size = new System.Drawing.Size(446, 231);
            this.AutorView.TabIndex = 0;
            // 
            // CarteView
            // 
            this.CarteView.ColumnHeadersHeightSizeMode = System.Windows.Forms.DataGridViewColumnHeadersHeightSizeMode.AutoSize;
            this.CarteView.Location = new System.Drawing.Point(667, 39);
            this.CarteView.Name = "CarteView";
            this.CarteView.RowHeadersWidth = 51;
            this.CarteView.RowTemplate.Height = 24;
            this.CarteView.Size = new System.Drawing.Size(337, 231);
            this.CarteView.TabIndex = 1;
            // 
            // titleBox
            // 
            this.titleBox.Location = new System.Drawing.Point(545, 312);
            this.titleBox.Name = "titleBox";
            this.titleBox.Size = new System.Drawing.Size(100, 22);
            this.titleBox.TabIndex = 2;
            // 
            // dateBox
            // 
            this.dateBox.Location = new System.Drawing.Point(545, 349);
            this.dateBox.Name = "dateBox";
            this.dateBox.Size = new System.Drawing.Size(178, 22);
            this.dateBox.TabIndex = 3;
            // 
            // idAutorBox
            // 
            this.idAutorBox.Location = new System.Drawing.Point(545, 386);
            this.idAutorBox.Name = "idAutorBox";
            this.idAutorBox.Size = new System.Drawing.Size(100, 22);
            this.idAutorBox.TabIndex = 4;
            // 
            // button1
            // 
            this.button1.Location = new System.Drawing.Point(842, 325);
            this.button1.Name = "button1";
            this.button1.Size = new System.Drawing.Size(75, 23);
            this.button1.TabIndex = 5;
            this.button1.Text = "Adaugare";
            this.button1.UseVisualStyleBackColor = true;
            this.button1.Click += new System.EventHandler(this.button1_Click);
            // 
            // stergereButton
            // 
            this.stergereButton.Location = new System.Drawing.Point(842, 365);
            this.stergereButton.Name = "stergereButton";
            this.stergereButton.Size = new System.Drawing.Size(75, 23);
            this.stergereButton.TabIndex = 6;
            this.stergereButton.Text = "Stergere";
            this.stergereButton.UseVisualStyleBackColor = true;
            this.stergereButton.Click += new System.EventHandler(this.stergereButton_Click);
            // 
            // updateButton
            // 
            this.updateButton.Location = new System.Drawing.Point(842, 404);
            this.updateButton.Name = "updateButton";
            this.updateButton.Size = new System.Drawing.Size(75, 23);
            this.updateButton.TabIndex = 7;
            this.updateButton.Text = "Update";
            this.updateButton.UseVisualStyleBackColor = true;
            this.updateButton.Click += new System.EventHandler(this.updateButton_Click);
            // 
            // Form1
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(8F, 16F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.ClientSize = new System.Drawing.Size(1080, 450);
            this.Controls.Add(this.updateButton);
            this.Controls.Add(this.stergereButton);
            this.Controls.Add(this.button1);
            this.Controls.Add(this.idAutorBox);
            this.Controls.Add(this.dateBox);
            this.Controls.Add(this.titleBox);
            this.Controls.Add(this.CarteView);
            this.Controls.Add(this.AutorView);
            this.Name = "Form1";
            this.Text = "Form1";
            this.Load += new System.EventHandler(this.Form1_Load_1);
            this.bindingChild.CurrentChanged += new System.EventHandler(this.bindingChild_CurrentChanged);
            ((System.ComponentModel.ISupportInitialize)(this.AutorView)).EndInit();
            ((System.ComponentModel.ISupportInitialize)(this.CarteView)).EndInit();
            this.ResumeLayout(false);
            this.PerformLayout();

        }

        #endregion

        private System.Windows.Forms.DataGridView AutorView;
        private System.Windows.Forms.DataGridView CarteView;
        private System.Windows.Forms.TextBox titleBox;
        private System.Windows.Forms.DateTimePicker dateBox;
        private System.Windows.Forms.TextBox idAutorBox;
        private System.Windows.Forms.Button button1;
        private System.Windows.Forms.Button stergereButton;
        private System.Windows.Forms.Button updateButton;
    }
}

