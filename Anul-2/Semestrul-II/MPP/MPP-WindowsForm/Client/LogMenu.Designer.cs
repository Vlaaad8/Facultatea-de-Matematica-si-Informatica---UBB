namespace MPP_WindowsForm;

partial class LogMenu
{
    /// <summary>
    ///  Required designer variable.
    /// </summary>
    private System.ComponentModel.IContainer components = null;

    /// <summary>
    ///  Clean up any resources being used.
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
        userBox = new System.Windows.Forms.TextBox();
        loginButton = new System.Windows.Forms.Button();
        registerButton = new System.Windows.Forms.Button();
        WizzAir = new System.Windows.Forms.Label();
        passwordBox = new System.Windows.Forms.TextBox();
        errorText = new System.Windows.Forms.Label();
        SuspendLayout();
        // 
        // userBox
        // 
        userBox.Location = new System.Drawing.Point(285, 171);
        userBox.Name = "userBox";
        userBox.Size = new System.Drawing.Size(201, 27);
        userBox.TabIndex = 0;
        // 
        // loginButton
        // 
        loginButton.Location = new System.Drawing.Point(262, 298);
        loginButton.Name = "loginButton";
        loginButton.Size = new System.Drawing.Size(143, 37);
        loginButton.TabIndex = 1;
        loginButton.Text = "Login";
        loginButton.UseVisualStyleBackColor = true;
        loginButton.Click += loginButton_Click;
        // 
        // registerButton
        // 
        registerButton.Location = new System.Drawing.Point(411, 296);
        registerButton.Name = "registerButton";
        registerButton.Size = new System.Drawing.Size(137, 39);
        registerButton.TabIndex = 2;
        registerButton.Text = "Register";
        registerButton.UseVisualStyleBackColor = true;
        // 
        // WizzAir
        // 
        WizzAir.Font = new System.Drawing.Font("Segoe UI", 16.2F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)0));
        WizzAir.Location = new System.Drawing.Point(325, 108);
        WizzAir.Name = "WizzAir";
        WizzAir.Size = new System.Drawing.Size(142, 60);
        WizzAir.TabIndex = 3;
        WizzAir.Text = "WizzAir";
        // 
        // passwordBox
        // 
        passwordBox.Location = new System.Drawing.Point(285, 231);
        passwordBox.Name = "passwordBox";
        passwordBox.Size = new System.Drawing.Size(201, 27);
        passwordBox.TabIndex = 4;
        passwordBox.PasswordChar = '*';
        // 
        // errorText
        // 
        errorText.Font = new System.Drawing.Font("Segoe UI", 13.8F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, ((byte)0));
        errorText.Location = new System.Drawing.Point(580, 376);
        errorText.Name = "errorText";
        errorText.Size = new System.Drawing.Size(175, 51);
        errorText.TabIndex = 5;
        // 
        // LogMenu
        // 
        AutoScaleDimensions = new System.Drawing.SizeF(8F, 20F);
        AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
        ClientSize = new System.Drawing.Size(800, 450);
        Controls.Add(errorText);
        Controls.Add(passwordBox);
        Controls.Add(WizzAir);
        Controls.Add(registerButton);
        Controls.Add(loginButton);
        Controls.Add(userBox);
        Text = "WizzAir";
        ResumeLayout(false);
        PerformLayout();
    }

    private System.Windows.Forms.Label errorText;

    private System.Windows.Forms.TextBox passwordBox;

    private System.Windows.Forms.TextBox userBox;
    private System.Windows.Forms.Button loginButton;
    private System.Windows.Forms.Button registerButton;
    private System.Windows.Forms.Label WizzAir;

    #endregion
}