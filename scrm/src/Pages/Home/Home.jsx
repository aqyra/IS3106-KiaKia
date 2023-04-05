import React from 'react';
import './home.css';
import HorizontalCard from '../../Components/Card/HorizontalCard/HorizontalCard';
import image from '../../Assets/img.jpg';

const dummyData = [
  {
    img: image,
    tripTag: 'Summer 2023 - 14 days trip',
    cardTitle: 'Japan',
    places: ["Tokyo", "Osaka", "Kyoto"],
    description: 'Lorem ipsum dolor sit amet, consectetur adipisicing elit. Voluptatibus a, nulla! Maiores et perferendis eaque, exercitationem praesentium nihil.'
  },
  {
    img: image,
    tripTag: 'Winter 2023 - 14 days trip',
    cardTitle: 'East Coast',
    places: ["New York", "Bronx", "Washington D.C."],
    description: 'Lorem ipsum dolor sit amet, consectetur adipisicing elit. Voluptatibus a, nulla! Maiores et perferendis eaque, exercitationem praesentium nihil.'
  },
  {
      img: image,
      tripTag: 'Winter 2023 - 14 days trip',
      cardTitle: 'East Coast',
      places: ["New York", "Bronx", "Washington D.C."],
      description: 'Lorem ipsum dolor sit amet, consectetur adipisicing elit. Voluptatibus a, nulla! Maiores et perferendis eaque, exercitationem praesentium nihil.'
    },
    {
      img: image,
      tripTag: 'Winter 2023 - 14 days trip',
      cardTitle: 'East Coast',
      places: ["New York", "Bronx", "Washington D.C."],
      description: 'Lorem ipsum dolor sit amet, consectetur adipisicing elit. Voluptatibus a, nulla! Maiores et perferendis eaque, exercitationem praesentium nihil.'
    },
]

const Home = () => {
  const horizontalCards = dummyData?.map((cardData) => (
    <HorizontalCard
      key={cardData.key}
      img={cardData.img}
      tripTag={cardData.tripTag}
      cardTitle={cardData.cardTitle}
      places={cardData.places}
      description={cardData.description} />
  ));
  return (
    <>
      <div className="container">
        <div className="pageTitle">
          <h1>Welcome,</h1>
          <h1>Natasha Rafaela</h1>
          <hr/>
          <p>What a fresh new day to start planning for your trip, isn't it? </p>
        </div>

        <div className="sec">
          <div className="secTitle">
            <h2>For your latest trip</h2>
          </div>
          <div className="subSec">
            <div className="subSecTitle">
              <h3>Top Places</h3><p>see more...</p>
            </div>
            <div className="list">
              {horizontalCards}
            </div>
          </div>
          <div className="subSec">
            <div className="subSecTitle">
              <h3>Top Itineraries</h3>
            </div>
            <div className="list">
              {horizontalCards}
            </div>
          </div>

        </div>



      </div>

    </>
  )
}

export default Home