import React, { useState, useEffect, useRef} from 'react'
import { useContentStore } from '../store/content'
import axios from 'axios';
import { Link } from 'react-router-dom';
import { SMALL_IMAGE_BASE_URL } from '../utils/constants';
import { ChevronLeft, ChevronRight } from 'lucide-react';

const MovieSlider = ({category}) => {
    const {contentType} = useContentStore();
    const [content, setContent] = useState(null);
    const [showArrows, setShowArrows] = useState(false);
    const sliderRef = useRef(null);

    useEffect(() => {
        const getContent = async () => {
            const res = await axios.get(`/api/v1/${contentType}/${category}`)
            setContent(res.data.content)
        }
        
        getContent();
        console.log(console.log());
    }, [contentType, category])

    const scrollLeft = () => { 
        if(sliderRef.current) {
            sliderRef.current.scrollBy({left: -sliderRef.current.offsetWidth, behavior: "smooth"});
        }
    }
    const scrollRight = () => {
        if(sliderRef.current) {
            sliderRef.current.scrollBy({left: sliderRef.current.offsetWidth, behavior: "smooth"});
        }
    }

    const formattedContentType = contentType === "movie" ? "Movies" : "TV Shows";
    const formattedCategoryName = category[0].toUpperCase() + category.replaceAll("_"," ").slice(1);

    return (
        <div className='text-white bg-black relative px-5 md:px-20' onMouseEnter={() => setShowArrows(true)} onMouseLeave={() => setShowArrows(false)}>
            <h2 className='text-2xl font-extrabold'>{formattedCategoryName} {formattedContentType}</h2>
            <div className='flex space-x-4 overflow-scroll mt-2 scrollbar-hide' ref={sliderRef}>
                {content?.map((item) => (
                    <Link to={`/watch/${item.id}`} className='min-w-[250px] relative group' key={item.id}>
                        <div className='rounded-lg overflow-hidden'>
                            <img src={SMALL_IMAGE_BASE_URL + item.backdrop_path} alt="movie image"  
                             className='transition-transform duration-300 ease-in-out group-hover:scale-125'/>
                        </div>
                        <p className='mt-1'>{item.title || item.name}</p>
                    </Link>
                ))}
            </div>
            {showArrows && (
                <div>
                    <button className='absolute top-1/2 -translate-y-1/2 left-5 md:left-24 flex items-center 
                                       justify-center size-12 rounded-full bg-black bg-opacity-50 
                                       hover:bg-opacity-75 text-white z-10' onClick={scrollLeft}>
                        <ChevronLeft size={24} />
                    </button>
                    <button className='absolute top-1/2 -translate-y-1/2 right-5 md:right-24 flex items-center 
                                       justify-center size-12 rounded-full bg-black bg-opacity-50 
                                       hover:bg-opacity-75 text-white z-10' onClick={scrollRight}>
                        <ChevronRight size={24} />
                    </button>
                </div>
            )}
        </div>
    )
}

export default MovieSlider
//  
                    //  