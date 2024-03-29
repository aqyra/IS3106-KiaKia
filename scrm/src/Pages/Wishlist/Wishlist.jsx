import React, { useParams, useState, useEffect } from "react";
import "./wishlist.css";
import HorizontalCard from "../../Components/Card/HorizontalCard/HorizontalCard";
import PlaceCard from "../../Components/Card/PlaceCard/PlaceCard";
import image from "../../Assets/img.jpg";
import singapore from "../../Assets/singapore.png";
import tokyo from "../../Assets/tokyo.jpg";
import { MdOutlineEdit } from "react-icons/md";
import Emoji from "a11y-react-emoji";
import { Button } from "@mui/material";
import Api from "../../Helpers/Api";
import TextField from "@mui/material/TextField";
import WishlistFolder from "./WishlistFolder";

// note: have to make wishlist inaccessible if not logged in!!
// dummy data for trips
const dummyData = [
  {
    img: image,
    tripTag: "Summer 2023 - 14 days trip",
    cardTitle: "Japan",
    places: ["Tokyo", "Osaka", "Kyoto"],
    description:
      "Lorem ipsum dolor sit amet, consectetur adipisicing elit. Voluptatibus a, nulla! Maiores et perferendis eaque, exercitationem praesentium nihil.",
  },
  {
    img: image,
    tripTag: "Winter 2023 - 14 days trip",
    cardTitle: "East Coast",
    places: ["New York", "Bronx", "Washington D.C."],
    description:
      "Lorem ipsum dolor sit amet, consectetur adipisicing elit. Voluptatibus a, nulla! Maiores et perferendis eaque, exercitationem praesentium nihil.",
  },
  {
    img: image,
    tripTag: "Winter 2023 - 14 days trip",
    cardTitle: "East Coast",
    places: ["New York", "Bronx", "Washington D.C."],
    description:
      "Lorem ipsum dolor sit amet, consectetur adipisicing elit. Voluptatibus a, nulla! Maiores et perferendis eaque, exercitationem praesentium nihil.",
  },
  {
    img: image,
    tripTag: "Winter 2023 - 14 days trip",
    cardTitle: "East Coast",
    places: ["New York", "Bronx", "Washington D.C."],
    description:
      "Lorem ipsum dolor sit amet, consectetur adipisicing elit. Voluptatibus a, nulla! Maiores et perferendis eaque, exercitationem praesentium nihil.",
  },
];

// dummy data for folders
const dummyData2 = [
  {
    id: 1,
    folderName: "Summer 2023",
    trips: dummyData,
  },
  {
    id: 2,
    folderName: "USA",
    trips: dummyData,
  },
  {
    id: 3,
    folderName: "Winter Trips",
    trips: dummyData,
  },
];

const placesDummyData = [
  {
    city: "Singapore",
    country: "Singapore",
    img: singapore,
  },
  {
    city: "Tokyo",
    country: "Japan",
    img: tokyo,
  },
  {
    city: "Tokyo",
    country: "Japan",
    img: tokyo,
  },
  {
    city: "Tokyo",
    country: "Japan",
    img: tokyo,
  },
];

const placesFolderDummyData = [
  {
    folderName: "Asia",
    places: placesDummyData,
  },
];

const Wishlist = (props) => {
  const [editMode, setEditMode] = useState(false);
  const [newFolderName, setNewFolderName] = useState("");
  const [selectedFolder, setSelectedFolder] = useState("");
  const [wishlistPlaces, setWishlistPlaces] = useState([]);
  const [name, setName] = useState("");
  const [selectedCard, setSelectedCard] = useState(null);

  useEffect(() => {
    reloadPlaces();
    console.log("yes");
  }, []);
  const reloadPlaces = () => {
    Api.getWishlistPlaces(props.userId)
      .then((response) => {
        if (!response.ok) {
          console.log("error");
          throw Error("could not fetch data");
        }
        return response.json();
      })
      .then((data) => {
        setWishlistPlaces(data);
        console.log(data);
      })
      .catch((error) => {
        console.log(error.message);
      });
    console.log(wishlistPlaces);
  };

  // get user
  useEffect(() => {
    Api.getUser(props.userId)
      .then((response) => response.json())
      .then((data) => {
        const name = data.name;
        setName(name);
      })
      .catch((error) => {
        console.log(
          `Error retrieving user data for user with ID ${props.userId}: ${error}`
        );
      });
  }, [props.userId]);

  // retrieve all saved folders
  const [folders, setFolders] = useState([]);
  useEffect(() => {
    const fetchData = async () => {
      try {
        const response = await Api.retrieveAllFolder(props.userId);
        const data = await response.json();
        setFolders(data);
      } catch (error) {
        console.log("Error while retrieving folders list");
      }
    };

    fetchData();
  }, [props.userId, folders, folders.folderName]);

  const handleSaveFolderName = async () => {
    // check if new folder name is there
    if (!newFolderName) {
      alert("Please enter a new folder name");
    }

    Api.updateFolderName(props.userId, props.folderId, newFolderName);
    setEditMode(false);
  };

  const handleNewFolderInput = (event) => {
    setNewFolderName(event.target.value);
  };

  const handlePlaceCardClick = async (data) => {
    setSelectedCard(data);

    if (!selectedCard) {
      alert("No card has been selected");
      return;
    }

    try {
      await Api.removeWishlistPlaceFromUser(
        props.userId,
        selectedCard.placeId
      ).then(() => {
        reloadPlaces();
      });
      alert("Removed from Wishlist");

      setSelectedCard(null);
    } catch (error) {
      console.log(error.message);
    }
  };

  const WishlistFolders = folders.map((data) => (
    <WishlistFolder
      saveFolderNameOnClick={handleSaveFolderName}
      selectedFolder={selectedFolder}
      folderNameOnChange={handleNewFolderInput}
      newFolderName={newFolderName}
      folderName={data.name}
      folderId={data.folderId}
      folder={data}
      userId={props.userId}
      trips={data.trips}
      {...data}
    />
  ));
  const placeCards = wishlistPlaces?.map((cardData) => (
    <PlaceCard
      key={cardData.id}
      {...cardData}
      userId={props.userId}
      wishlist={true}
      onClick={() => {
        handlePlaceCardClick(cardData);
      }}
    />
  ));

  return (
    <>
      <div className="container">
        <div className="pageTitle">
          <h1>
            {name}'s Wishlist <Emoji symbol="✨" label="sparkle emoji" />
          </h1>
        </div>

        <div className="sec">
          <div className="secTitle">
            <h2>Trips You Love</h2>
            <div className="wishlist-folder">
              {WishlistFolders}

              <>
                {folders.length < 1 ? (
                  <h3 className="blank-msg">
                    There's no saved trips here. Save some Trips you find
                    interesting!
                  </h3>
                ) : (
                  <></>
                )}
              </>
            </div>
          </div>

          <div className="secTitle">
            <h2>Places You Love</h2>
            <div className="subSecTitle">
              {placeCards}

              <>
                {wishlistPlaces.length < 1 ? (
                  <h3 className="blank-msg">
                    There's no saved places here. Save some Trips you find
                    interesting!
                  </h3>
                ) : (
                  <></>
                )}
              </>
            </div>
          </div>
        </div>
      </div>
    </>
  );
};

export default Wishlist;
