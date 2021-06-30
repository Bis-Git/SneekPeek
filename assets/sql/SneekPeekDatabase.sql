CREATE DATABASE SneakPeekDatabase

GO

USE SneakPeekDatabase

GO

--TABLICE

CREATE TABLE [User]
(
	IDUser INT PRIMARY KEY IDENTITY,
	FirstName NVARCHAR(50) NOT NULL,
	LastName NVARCHAR(50) NOT NULL,
	Username NVARCHAR(50) NOT NULL,
	[Password] NVARCHAR(50) NOT NULL,
	Email NVARCHAR(50) NOT NULL,
	IsAdmin BIT NOT NULL
)

GO

INSERT INTO [User] VALUES ('Admin', 'Admin', 'Admin', '0000', 'admin@mail.com', 1)

GO

CREATE TABLE Movie
(
	IDMovie INT PRIMARY KEY IDENTITY,
	Title NVARCHAR(300),
	PublishDate NVARCHAR(50),
	[Description] NVARCHAR(max),
	Genre NVARCHAR(300),
	PicturePath NVARCHAR(100),
	OrigName NVARCHAR(100)
)

GO

CREATE TABLE Actor
(
	IDActor INT PRIMARY KEY IDENTITY,
	FirstName NVARCHAR(300),
	LastName NVARCHAR(300),
	PicturePath nvarchar(100)
)

GO


CREATE TABLE Movie_Actors
(
	IDMovieActor INT PRIMARY KEY IDENTITY,
	MovieID INT FOREIGN KEY REFERENCES Movie(IDMovie),
	ActorID INT FOREIGN KEY REFERENCES Actor(IDActor)
)

GO

CREATE TABLE Director
(
	IDDirector INT PRIMARY KEY IDENTITY,
	FirstName NVARCHAR(300),
	LastName NVARCHAR(300),
	PicturePath nvarchar(100)
)

GO

CREATE TABLE Movie_Directors
(
	IDMovieDirector INT PRIMARY KEY IDENTITY,
	MovieID INT FOREIGN KEY REFERENCES Movie(IDMovie),
	DirectorID INT FOREIGN KEY REFERENCES Director(IDDirector)
)

GO

--PROCEDURE
--USER
CREATE PROC spFindUser
	@Username NVARCHAR(50),
	@Password NVARCHAR(50)
AS
BEGIN
	SELECT * FROM [User]
	WHERE @Username = Username AND @Password = [Password]
END

GO

CREATE PROC spUpdateUser
	@OldUsername NVARCHAR(50),
	@OldPassword NVARCHAR(50),
	@FirstName NVARCHAR(50),
	@LastName NVARCHAR(50),
	@Email NVARCHAR(50),
	@Username NVARCHAR(50),
	@Password NVARCHAR(50)
AS
BEGIN
	UPDATE [User]
	SET FirstName = @FirstName, LastName = @LastName, Email = @Email, Username = @Username, [Password] = @Password
	WHERE Username = @OldUsername AND [Password] = @OldPassword
END

GO

CREATE PROC spCreateUser
	@Firstname NVARCHAR(50),
	@Lastname NVARCHAR(50),
	@Username NVARCHAR(50),
	@Password NVARCHAR(50),
	@Email NVARCHAR(50),
	@IsAdmin BIT,
	@Id INT OUTPUT
AS
BEGIN
	INSERT INTO [User] VALUES(@Firstname, @Lastname, @Username, @Password, @Email, @IsAdmin)
	SET @Id = SCOPE_IDENTITY()
END

GO

CREATE PROC spFindUserByEmail
	@Email NVARCHAR(50)
AS
BEGIN
	SELECT * FROM [User] WHERE Email = @Email
END

GO

CREATE PROC spFindUserByUsername
	@Username NVARCHAR(50)
AS
BEGIN TRAN
	SELECT * FROM [User] WHERE Username = @Username
COMMIT

GO
--MOVIE

CREATE PROC spCreateMovie
	@Title NVARCHAR(100),
	@PublishDate NVARCHAR(100),
	@Description NVARCHAR(MAX),
	@Genre NVARCHAR(100),
	@PicturePath NVARCHAR(100),
	@OrigName NVARCHAR(100),
	@Id INT OUT,
	@New BIT OUT
AS
BEGIN
	IF(NOT EXISTS(SELECT * FROM Movie WHERE OrigName = @OrigName))
	BEGIN
		INSERT INTO Movie VALUES (@Title, @PublishDate, @Description, @Genre, @PicturePath, @OrigName)
		SET @Id = SCOPE_IDENTITY()
		SET @New = 1
	END
	ELSE
	BEGIN
		SELECT @Id = IDMovie FROM Movie WHERE OrigName = @OrigName
		SET @New = 0
	END
END

GO

CREATE PROC spSelectAllMovies
AS
BEGIN
	SELECT * FROM Movie
END

GO

CREATE PROC spSelectMovie
	@Id INT
AS
BEGIN
	SELECT *
	FROM Movie
	WHERE IDMovie = @Id
END

GO

CREATE PROC spUpdateMovie
	@Id INT,
	@Title NVARCHAR(100),
	@PublishDate NVARCHAR(100),
	@Description NVARCHAR(MAX),
	@Genre NVARCHAR(100),
	@PicturePath NVARCHAR(100),
	@OrigName NVARCHAR(100)
AS
BEGIN
	UPDATE Movie
	SET Title = @Title,
		PublishDate = @PublishDate,
		[Description] = @Description,
		Genre = @Genre,
		PicturePath = @PicturePath,
		OrigName = @OrigName
	WHERE IDMovie = @Id
END

GO

CREATE PROC spDeleteMovie
	@Id INT
AS
BEGIN
	DELETE FROM Movie_Actors
	WHERE MovieID = @Id
	DELETE FROM Movie_Directors
	WHERE MovieID = @Id
	DELETE FROM Movie
	WHERE IDMovie = @Id
END

GO

--ACTOR

CREATE PROC spCreateActor
	@FirstName NVARCHAR(300),
	@LastName NVARCHAR(300),
	@PicturePath NVARCHAR(100),
	@Id INT OUTPUT
AS
BEGIN
	IF((SELECT COUNT(*) FROM Actor WHERE FirstName=@FirstName AND LastName = @LastName) = 0)
	BEGIN
		IF(@PicturePath is null)
		BEGIN
			INSERT INTO Actor VALUES(@FirstName, @LastName, 'assets/graphics/defaultImg.jpg')
		END
		ELSE
		BEGIN
			INSERT INTO Actor VALUES(@FirstName, @LastName, @PicturePath)
		END
		SET @Id = SCOPE_IDENTITY()
	END
	ELSE
	BEGIN
		SELECT @Id = IDActor FROM Actor WHERE FirstName = @FirstName AND LastName = @LastName
	END
END

GO

CREATE PROC spFindActor
	@MovieID INT
AS
BEGIN
	SELECT IDActor, FirstName, LastName, PicturePath
	FROM Actor
	INNER JOIN Movie_Actors
	ON ActorID = IDActor
	WHERE MovieID = @MovieID
END

GO

CREATE PROC spSelectActors
AS
BEGIN
	SELECT * FROM Actor
END

GO

CREATE PROC spSelectActor
	@Id int
AS
BEGIN
	SELECT * FROM Actor WHERE IDActor = @Id
END

GO

CREATE PROC spUpdateActor
	@Id int,
	@FirstName NVARCHAR(300),
	@LastName NVARCHAR(300),
	@PicturePath NVARCHAR(300)
AS
BEGIN
	UPDATE Actor
	SET FirstName = @FirstName, LastName = @LastName, PicturePath = @PicturePath
	WHERE IDActor = @Id
END

GO

CREATE PROC spDeleteActor
	@Id int
AS
BEGIN
	DELETE FROM Movie_Actors
	WHERE ActorID = @Id

	DELETE FROM Actor
	WHERE IDActor = @Id
END

GO


--DIRECTOR

CREATE PROC spSelectDirectors
AS
BEGIN
	SELECT * FROM Director
END

GO

CREATE PROC spSelectDirector
	@Id int
AS
BEGIN
	SELECT * FROM Director WHERE IDDirector = @Id
END


GO

CREATE PROC spUpdateDirector
	@Id int,
	@FirstName NVARCHAR(300),
	@LastName NVARCHAR(300),
	@PicturePath NVARCHAR(300)
AS
BEGIN
	UPDATE Director
	SET FirstName = @FirstName, LastName = @LastName, PicturePath = @PicturePath
	WHERE IDDirector = @Id
END


GO

CREATE PROC spCreateDirector
	@FirstName NVARCHAR(300),
	@LastName NVARCHAR(300),
	@PicturePath NVARCHAR(100),
	@Id INT OUTPUT
AS
BEGIN
	IF((SELECT COUNT(*) FROM Director WHERE FirstName = @FirstName AND LastName = @LastName) = 0)
	BEGIN
		IF(@PicturePath is null)
		BEGIN
			INSERT INTO Director VALUES(@FirstName, @LastName, 'assets/graphics/defaultImg.jpg')
		END
		ELSE
		BEGIN
			INSERT INTO Director VALUES(@FirstName, @LastName, @PicturePath)
		END
		SET @Id = SCOPE_IDENTITY()
	END
	ELSE
	BEGIN
		SELECT @Id = IDDirector FROM Director WHERE FirstName = @FirstName AND LastName = @LastName
	END
END

GO

CREATE PROC spFindDirector
	@MovieID INT
AS
BEGIN
	SELECT FirstName, LastName, IDDirector, PicturePath
	FROM Director
	INNER JOIN Movie_Directors
	ON DirectorID = IDDirector
	WHERE MovieID = @MovieID
END

GO

CREATE PROC spDeleteDirector
	@Id int
AS
BEGIN
	DELETE FROM Movie_Directors
	WHERE DirectorID = @Id

	DELETE FROM Director
	WHERE IDDirector = @Id
END

GO

--DATABASE

CREATE PROC spLinkActorsToMovies
	@MovieID INT,
	@ActorID INT
AS
BEGIN
	INSERT INTO Movie_Actors VALUES(@MovieID, @ActorID)
END

GO

CREATE PROC spLinkDirectorsToMovies
	@MovieID INT,
	@DirectorID INT
AS
BEGIN
	INSERT INTO Movie_Directors VALUES(@MovieID, @DirectorID)
END

GO

CREATE PROC spDeleteActorsMovie
	@ID int
AS
	DELETE FROM Movie_Actors WHERE MovieID = @ID

GO

CREATE PROC spDeleteDirectorsMovie
	@ID int
AS
	DELETE FROM Movie_Directors WHERE MovieID = @ID

GO

CREATE PROC spClearData
AS
BEGIN TRAN
	DELETE FROM Movie_Directors
	DELETE FROM Director
	DELETE FROM Movie_Actors
	DELETE FROM Actor
	DELETE FROM Movie
COMMIT

